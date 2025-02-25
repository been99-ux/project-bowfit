package com.example.finaln.dto;

import com.example.finaln.entity.FaqNotice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FaqNoticeDto {

    private Long id;
    private String title;     // 게시글 제목
    private String content;   // 게시글 내용
    private String imageUrl;  // 이미지 경로
    private String topic;     // 게시글 주제 (FAQ, NOTICE)
    private String writerName; // 작성자 이름
    private LocalDateTime createdAt; // 작성 날짜

    public static FaqNoticeDto fromEntity(FaqNotice faqNotice) {
        FaqNoticeDto dto = new FaqNoticeDto();
        dto.setId(faqNotice.getId());
        dto.setTitle(faqNotice.getTitle());
        dto.setContent(faqNotice.getContent());
        dto.setImageUrl(faqNotice.getImageUrl());
        dto.setTopic(faqNotice.getTopic().name());
        dto.setWriterName(faqNotice.getMember().getName());
        dto.setCreatedAt(faqNotice.getCreatedAt());
        return dto;
    }
}
