package com.example.finaln.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FrontController {
// ------------------------------ 시작페이지
    @GetMapping("/intro")
    public String index() {
        return "index";
    }
// --------------------------------헤더
    @GetMapping("/header")
    public String header() {
        return "common/header";
    }
// --------------------------------푸터
    @GetMapping("/footer")
    public String footer() {
        return "common/footer";
    }
// ------------------------------------------------------------


// -------------------------------- 장바구니
    @GetMapping("/cartlist")
    public String cartlist() {
        return "productPage/cartlist";
    }

// -------------------------------- 찜목록
    @GetMapping("/wishlist")
    public String wishlist() {  return "productPage/wishlist"; }

// -------------------------------- 상품페이지
    @GetMapping("/productshop")
    public String productshop() {
        return "productPage/productshop";
    }

// -------------------------------- 최고상품페이지
    @GetMapping("/productbest")
    public String productbest() {
        return "productPage/productbest";
    }

// --------------------------------프로덕트정보
    @GetMapping("/productinfo")
    public String productinfo() {
        return "productPage/productInfo";
    }

// --------------------------------회사 intro
    @GetMapping("/companyintro")
    public String companyintro() {

        return "/about/companyIntro";
    }
// --------------------------------회사 소개
    @GetMapping("/companystory")
    public String companystory() {
        return "/about/companyStory";
    }
// --------------------------------입점신청
    @GetMapping("/appform")
    public String appform() {
        return "about/appForm";
    }
// -------------------------------- 마이페이지
    @GetMapping("/mypage")
    public String myPage() {
        return "myPage/myPage";
    }
// -------------------------------- 마이페이지/ 마일리지 히스토리
    @GetMapping("/mypage/mileage")
    public String mileage() {
        return "myPage/mileageHistory";
    }
// -------------------------------- 마이페이지/ 쿠폰 히스토리
    @GetMapping("/mypage/coupon")
    public String coupon() {
        return "myPage/couponHistory";
    }
// -------------------------------- 주문조회 페이지
    @GetMapping("/mypage/orderinquiry")
    public String orderinquiry() {
        return "myPage/orderInquiry";
    }


// -------------------------------- 커뮤니티 페이지
    @GetMapping("/community")
    public String community() {
        return "community/community";
    }

    // 이용안내 faq 자주묻는질문 페이지
    @GetMapping("/community/faq")
    public String faq() {
        return "community/faq";
    }
    // notice 공지사항 페이지
    @GetMapping("/community/notice")
    public String notice() {
        return "community/notice";
    }
    // 리뷰페이지
    @GetMapping("/community/review")
    public String review() {
        return "community/review";
    }

    @GetMapping("/community/postManagement")
    public String postManagement() {
        return "community/postManagement";
    }



    @GetMapping("/qna/11")
    public String qnaContent() {
        return "community/qnaContent";
    }
    @GetMapping("/errorpage")
    public String error() {
        return "/errorPage";
    }

    @GetMapping("/prepare")
    public String prepare() {
        return "prepare";
    }


    // --------------------------------- 구매 페이지
    @GetMapping("/productshop/pay") // GET 방식
    public String pay(@RequestParam("productId") int productId,
                      @RequestParam("productName") String productName,
                      @RequestParam("productPrice") int productPrice,
                      @RequestParam("productColor") String productColor,
                      @RequestParam("productSize") String productSize) {
        // ... 상품 정보 처리 및 결제 페이지 로직 ...
        System.out.println("상품 ID: " + productId);
        System.out.println("상품 이름: " + productName);
        System.out.println("상품 가격: " + productPrice);
        System.out.println("상품 색상: " + productColor);
        System.out.println("상품 사이즈: " + productSize);
        return "payPage/pay";
    }


}
