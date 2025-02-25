package com.example.finaln.controller;

import com.example.finaln.dto.FaqNoticeDto;
import com.example.finaln.entity.FaqNotice;
import com.example.finaln.service.FaqNoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/faq-notice")
@RequiredArgsConstructor
public class FaqNoticeController {

    private final FaqNoticeService faqNoticeService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("topic") String topic,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session) {

        System.out.println(title);
        System.out.println(content);
        System.out.println(topic);
        System.out.println(imageFile);

        // 세션에서 로그인된 사용자 정보 가져오기
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 가져오기

        if (loginId == null) {
            return ResponseEntity.badRequest().body("로그인이 필요합니다.");
        }
        try {
            faqNoticeService.createPost(loginId, title, content, topic, imageFile);
            return ResponseEntity.ok("success");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FaqNoticeDto>> getAllPosts() {
        List<FaqNoticeDto> posts = faqNoticeService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 공지사항 목록
    @GetMapping("/list/notice")
    public ResponseEntity<Page<FaqNoticeDto>> getPagedNoticePosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        System.out.println("page: " + page);
        System.out.println("size: " + size);
        Page<FaqNoticeDto> posts = faqNoticeService.getPagedPostsByTopic(FaqNotice.Topic.NOTICE, page, size);
        return ResponseEntity.ok(posts);
    }

    // FAQ 목록
    @GetMapping("/list/faq")
    public ResponseEntity<Page<FaqNoticeDto>> getPagedFaqPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Page<FaqNoticeDto> posts = faqNoticeService.getPagedPostsByTopic(FaqNotice.Topic.FAQ, page, size);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/latest")
    public ResponseEntity<List<FaqNoticeDto>> getLatestPosts() {
        List<FaqNoticeDto> latestPosts = faqNoticeService.getLatestNoticePosts(5); // 최근 5개 가져오기
        return ResponseEntity.ok(latestPosts);
    }

    // 새로운 FAQ 게시글 가져오는 메서드
    @GetMapping("/latest-faq")
    public ResponseEntity<List<FaqNoticeDto>> getLatestFaqPosts() {
        List<FaqNoticeDto> latestFaqPosts = faqNoticeService.getLatestFaqPosts(5); // 최신 5개 FAQ 게시글 가져오기
        return ResponseEntity.ok(latestFaqPosts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FaqNoticeDto>> searchPosts(
            @RequestParam String topic,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        FaqNotice.Topic topicEnum = FaqNotice.Topic.valueOf(topic.toUpperCase());
        Page<FaqNoticeDto> searchResults = faqNoticeService.searchPostsByTopic(topicEnum, query, page, size);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<FaqNoticeDto>> getPagedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "topic", required = false, defaultValue = "") String topic) {
        FaqNotice.Topic topicEnum = topic.isEmpty() ? null : FaqNotice.Topic.valueOf(topic.toUpperCase());
        Page<FaqNoticeDto> posts;

        if (!query.isEmpty() && topicEnum != null) {
            posts = faqNoticeService.searchPostsByTopic(topicEnum, query, page, size);
        } else if (!query.isEmpty()) {
            posts = faqNoticeService.searchPosts(query, page, size);
        } else if (topicEnum != null) {
            posts = faqNoticeService.getPagedPostsByTopic(topicEnum, page, size);
        } else {
            posts = faqNoticeService.getPagedPosts(page, size);
        }

        return ResponseEntity.ok(posts);

    }

    @GetMapping("/post/{id}")
    public ResponseEntity<FaqNoticeDto> getPostById(@PathVariable Long id) {
        FaqNoticeDto post = faqNoticeService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    // 📌 게시글 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<FaqNoticeDto> updateFaqNotice(
            @PathVariable Long id,
            @RequestBody FaqNoticeDto requestDto) {
        FaqNoticeDto updatedPost = faqNoticeService.updateFaqNotice(id, requestDto.getTitle(), requestDto.getContent());
        return ResponseEntity.ok(updatedPost);
    }

    // 📌 게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deleteFaqNotice(@PathVariable Long id) {
        faqNoticeService.deleteFaqNotice(id);
        return ResponseEntity.noContent().build();
    }






}







