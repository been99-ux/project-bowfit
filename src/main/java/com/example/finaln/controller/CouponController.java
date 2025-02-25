package com.example.finaln.controller;

import com.example.finaln.entity.Coupon;
import com.example.finaln.repository.CouponRepository;
import com.example.finaln.service.CouponService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 목록 보기
    @GetMapping("/myPage/couponHistory")
    public String couponHistory(Model model, @SessionAttribute(name = "memberId", required = false) Long memberId) {
        if (memberId == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리디렉트
        }

        List<Coupon> coupons = couponService.getCouponsByMemberId(memberId); // ✅ 쿠폰 목록 조회
        model.addAttribute("coupons", coupons);
        model.addAttribute("memberId", memberId);
        model.addAttribute("memberId", memberId);
        return "myPage/couponHistory"; // Thymeleaf에서 사용할 HTML 파일명
    }


    @GetMapping("/getMemberId")
    @ResponseBody
    public Map<String, Object> getMemberId(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        Map<String, Object> response = new HashMap<>();

        if (memberId != null) {
            response.put("memberId", memberId);
        } else {
            response.put("memberId", null);
        }

        return response;
    }

    @GetMapping("/myPage/couponCount")
    @ResponseBody  // ✅ JSON 형식으로 반환
    public int getCouponCount(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        System.out.println("쿠폰 조회 - 세션 memberId: " + memberId);

        if (memberId != null) {
            return couponService.getCouponCountByMember(memberId);
        }
        return 0;
    }



}
