package com.example.finaln.controller;

import com.example.finaln.dto.QnaPostDto;
import com.example.finaln.entity.Member;
import com.example.finaln.entity.QnaPost;
import com.example.finaln.service.QnaPostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class QnaPostController {

    private final QnaPostService qnaPostService;

    public QnaPostController(QnaPostService qnaPostService) {
        this.qnaPostService = qnaPostService;
    }

    // 질문과 응답 페이지
    @GetMapping("/qna")
    public String qna(Model model) {
        List<QnaPostDto> qnaPosts = qnaPostService.getAllQnaPosts();
        model.addAttribute("qnaPosts", qnaPosts);
        return "community/qna";
    }

    // 최신 Q&A 게시물 5개 가져오기 (JSON 응답)
    @GetMapping("/api/qna/latest")
    public ResponseEntity<List<QnaPostDto>> getLatestQnaPosts() {
        List<QnaPostDto> latestQnaPosts = qnaPostService.getLatestQnaPosts(5);
        return ResponseEntity.ok(latestQnaPosts);
    }


    @GetMapping("/user/form")
    public String form(Model model) {
        model.addAttribute("qnaPost", new QnaPost());
        return "/community/userForm";
    }

    @PostMapping("/qna/create")
    public String createQnaPost(@ModelAttribute QnaPostDto qnaPostDto,
                                @RequestParam("fileInput") MultipartFile fileInput,
                                @RequestParam("secret") int secret,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 정보가 없습니다.");
            return "redirect:/login";
        }

        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "회원 정보가 유효하지 않습니다.");
            return "redirect:/login";
        }

        try {
            QnaPost qnaPost = new QnaPost();
            qnaPost.setTitle(qnaPostDto.getTitle());
            qnaPost.setContent(qnaPostDto.getContent());
            qnaPost.setPassword(qnaPostDto.getPassword());
            qnaPost.setSecret(secret == 1);
            qnaPost.setMember(member);

            // 🛠 createQnaPost가 QnaPost를 반환하도록 수정했으니, 저장된 객체를 받는다.
            QnaPost savedPost = qnaPostService.createQnaPost(qnaPost, fileInput);
            qnaPostDto.setImageUrl(savedPost.getImageUrl()); // DTO에 경로 저장

            return "redirect:/qna";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "게시글 작성 중 오류가 발생했습니다.");
            return "redirect:/community/userForm";
        }
    }

}