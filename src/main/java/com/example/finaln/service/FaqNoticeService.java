package com.example.finaln.service;

import com.example.finaln.dto.FaqNoticeDto;
import com.example.finaln.entity.FaqNotice;
import com.example.finaln.entity.Member;
import com.example.finaln.repository.FaqNoticeRepository;
import com.example.finaln.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqNoticeService {
    private final FaqNoticeRepository faqNoticeRepository;
    private final MemberRepository memberRepository;

    public void createPost(String loginId, String title, String content, String topic, MultipartFile imageFile) throws IOException {
        // 작성자 찾기
        Member member = Optional.ofNullable(memberRepository.findByLoginId(loginId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

// 이미지 저장 (이미지 경로 저장)
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            String uploadDir = "C:/upload"; // 저장할 경로
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File file = new File(uploadDir, fileName); // 파일 저장 위치 지정

            // 디렉토리가 없으면 생성
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 파일 저장
            imageFile.transferTo(file);

            // ✅ 웹에서 접근할 수 있도록 URL 반환
            imageUrl = "/upload/" + fileName;
        }


        // 게시글 저장
        FaqNotice faqNotice = new FaqNotice();
        faqNotice.setTitle(title);
        faqNotice.setContent(content);
        faqNotice.setTopic(FaqNotice.Topic.valueOf(topic.toUpperCase()));
        faqNotice.setImageUrl(imageUrl);
        faqNotice.setMember(member);

        faqNoticeRepository.save(faqNotice);
    }

    public List<FaqNoticeDto> getAllPosts() {
        return faqNoticeRepository.findAll().stream()
                .map(FaqNoticeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FaqNoticeDto> getPagedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return faqNoticeRepository.findAll(pageable).map(FaqNoticeDto::fromEntity);
    }


    // 특정 topic의 게시글 조회 (예: NOTICE)
    public List<FaqNoticeDto> getPostsByTopic(FaqNotice.Topic topic) {
        List<FaqNotice> posts = faqNoticeRepository.findByTopic(topic);
        return posts.stream().map(FaqNoticeDto::fromEntity).collect(Collectors.toList());
    }

    // 커뮤니티 페이지의 공지사항 박스 최근 5개글
    public List<FaqNoticeDto> getLatestNoticePosts(int limit) {
        // 1. Define the Topic enum value for NOTICE
        FaqNotice.Topic topic = FaqNotice.Topic.NOTICE;

        // 2. Use a custom query in the repository
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return faqNoticeRepository.findTop5ByTopicOrderByCreatedAtDesc(topic, pageable).stream()
                .map(FaqNoticeDto::fromEntity)
                .collect(Collectors.toList());
    }


    // 커뮤니티 페이지의 FAQ 박스 최근 5개글
    public List<FaqNoticeDto> getLatestFaqPosts(int limit) {
        // 1. Define the Topic enum value for NOTICE
        FaqNotice.Topic topic = FaqNotice.Topic.FAQ;

        // 2. Use a custom query in the repository
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return faqNoticeRepository.findTop5ByTopicOrderByCreatedAtDesc(topic, pageable).stream()
                .map(FaqNoticeDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FaqNoticeDto> getPagedPostsByTopic(FaqNotice.Topic topic, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FaqNotice> faqNoticePage = faqNoticeRepository.getPagedPostsByTopic(topic, pageable);
        return faqNoticePage.map(FaqNoticeDto::fromEntity);
    }

    public Page<FaqNoticeDto> searchPostsByTopic(FaqNotice.Topic topic, String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FaqNotice> searchResults = faqNoticeRepository.searchPosts(topic, query, pageable);
        return searchResults.map(FaqNoticeDto::fromEntity);
    }

    public Page<FaqNoticeDto> searchPosts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return faqNoticeRepository.searchPosts(query, pageable).map(FaqNoticeDto::fromEntity);
    }


    public FaqNoticeDto getPostById(Long id) {
        return faqNoticeRepository.findById(id)
                .map(FaqNoticeDto::fromEntity)
                .orElse(null);
    }


    // 📌 게시글 수정 (제목, 내용만 수정 가능)
    @Transactional
    public FaqNoticeDto updateFaqNotice(Long id, String title, String content) {
        FaqNotice faqNotice = faqNoticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));

        faqNotice.setTitle(title);
        faqNotice.setContent(content);

        faqNoticeRepository.save(faqNotice);
        return FaqNoticeDto.fromEntity(faqNotice);
    }

    // 📌 게시글 삭제
    @Transactional
    public void deleteFaqNotice(Long id) {
        if (!faqNoticeRepository.existsById(id)) {
            throw new RuntimeException("해당 ID의 게시글이 존재하지 않습니다. ID: " + id);
        }
        faqNoticeRepository.deleteById(id);
    }




}
