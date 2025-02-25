package com.example.finaln.controller;

import com.example.finaln.dto.MemberUpdateDto;
import com.example.finaln.entity.Member;
import com.example.finaln.entity.MemberDetail;
import com.example.finaln.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;

    }

//----------------------------회원정보수정-------------------------------------------------

    @GetMapping("/mypage/profile")
    public String updateMemberForm(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = memberService.getMemberByLoginId((String) session.getAttribute("loginId"));
        model.addAttribute("member", member);
        return "/myPage/profile";
    }

    @PutMapping("/member/update")
    public ResponseEntity<Map<String, String>> updateMember(@RequestBody MemberUpdateDto memberUpdateDto, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        Map<String, String> response = new HashMap<>();

        if (memberId == null) {
            response.put("message", "로그인 정보가 없습니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            memberService.updateMember(memberId, memberUpdateDto);
            response.put("message", "회원정보가 수정되었습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "회원정보 수정에 실패했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//---------------------- 회원 가입 ------------------------------------------------//
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("member", new Member()); // 모델 객체 생성 및 전달
        return "/loginPage/joinMembership";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute Member member , HttpServletRequest request) { // @ModelAttribute 추가
        String email = request.getParameter("email"); // email 파라미터 추가
        member.setEmail(email); // email 설정
        String phone = request.getParameter("phone"); // phone 파라미터 추가
        member.setPhone(phone); // phone 설정

        // 회원 가입 시 role 을 USER 로 설정
        member.setRole(Member.Role.USER);

        memberService.registerUser(member);
        return "redirect:/login";
    }
    //---------------------- 회원가입 아이디 중복확인 ------------------------------------------------//
    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String loginId) {
        if (!isValidLoginId(loginId)) { // 유효성 검사 실패
            return "invalid";
        }

        if (memberService.isLoginIdDuplicate(loginId)) { // 중복된 아이디
            return "duplicate";
        }

        return "available"; // 사용 가능한 아이디
    }

    private boolean isValidLoginId(String loginId) {
        // 아이디 유효성 검사 로직 (영문소문자/숫자, 4~16자)
        String regex = "^[a-z0-9]{4,16}$";
        return loginId.matches(regex);
    }
// ------------------- 이메일과 휴대폰 중복 검사 ---------------------------------

    // 이메일 중복 검사
    @PostMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    // 휴대폰 번호 중복 검사
    @PostMapping("/check-phone")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@RequestParam String phone) {
        boolean exists = memberService.isPhoneExists(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


//---------------------- 회원가입 비밀번호 유효성 ------------------------------------------------//
    @GetMapping("/checkPassword")
    @ResponseBody
    public String checkPassword(@RequestParam String password) {
        if (!isValidPassword(password)) { // 유효성 검사 실패
            return "invalid";
        }
        return "valid"; // 유효한 비밀번호
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*\\d|.*[\\W_])|(?=.*\\d)(?=.*[a-z]|.*[\\W_])|(?=.*[\\W_])(?=.*[a-z]|.*\\d).{2,}$";
        return password.matches(regex);
    }
//---------------------- 로그인 ------------------------------------------------//
    @GetMapping("/login")
    public String loginForm() {
        return "/loginPage/login";
    }
    @GetMapping("/main")
    public String mainPage(Model model) {
        // 필요한 데이터 모델에 추가 (예: 로그인 사용자 정보)
        return "main"; // main.html 템플릿
    }

//---------------------- 로그인 ------------------------------------------------//
    @PostMapping("/login")
    public String login(@RequestParam String loginId, @RequestParam String password, HttpSession session, Model model) {
        Member member = memberService.getMemberByLoginId(loginId);

        if (member != null) {
            System.out.println("Member role: " + member.getRole()); // role 값 로그 출력
            System.out.println("Entered password: " + password); // 입력한 비밀번호 로그 출력
            System.out.println("DB password: " + member.getPassword()); // DB에 저장된 비밀번호 로그 출력
            System.out.println("DB email: " + member.getEmail());
        }

        if (member != null && member.getPassword().equals(password)) {
            session.setAttribute("member", member);
            session.setAttribute("memberId", member.getId());  // ✅ memberId 저장
            session.setAttribute("loginId", loginId);
            session.setAttribute("role", member.getRole()); // 세션에 role 정보 저장

            // 👉 세션 저장된 값 확인 로그 추가
            System.out.println("로그인 성공 - memberId: " + session.getAttribute("memberId"));

            if (member.getRole() == Member.Role.ADMIN) {
                return "redirect:/adminpage"; // 관리자 메인 페이지로 리다이렉트
            } else {
                return "redirect:/main"; // 사용자 메인 페이지로 리다이렉트
            }
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "/loginPage/login";
        }
    }
//---------------------- 로그아웃 ------------------------------------------------//
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login";
    }



//---------------------------회원탈퇴--------------------------------------------------
@DeleteMapping("/member/withdraw") // DELETE method 사용
public ResponseEntity<String> withdrawMember(HttpSession session) {
    Long memberId = (Long) session.getAttribute("memberId");

    if (memberId == null) {
        return new ResponseEntity<>("로그인 정보가 없습니다.", HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    try {
        memberService.withdrawMember(memberId);
        session.invalidate(); // 탈퇴 후 세션 무효화
        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK); // 200 OK
    } catch (Exception e) {
        return new ResponseEntity<>("회원 탈퇴에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }
}
//------------------------관리자 페이지 컨트롤러 섹션---------------------------------------------

    //-------------------- 회원 정보 불러오기 ---------------------------------------------
    @GetMapping("adminpage")
    public String memberList(Model model,
                             @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                              String searchKeyword) {

        Page<Member> userMembers = null;

        if (searchKeyword == null) {
            userMembers = memberService.getUserMembers(pageable);
        }else{
            userMembers = memberService.memberSearchList(searchKeyword, pageable);
        }

        model.addAttribute("memlist", userMembers);
        model.addAttribute("currentPage", userMembers.getNumber());  // 현재 페이지 번호
        model.addAttribute("totalPages", userMembers.getTotalPages());  // 전체 페이지 수

        return "/adminPage";
    }



}

