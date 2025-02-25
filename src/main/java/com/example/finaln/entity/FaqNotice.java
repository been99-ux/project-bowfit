package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FaqNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    private String imageUrl; // 이미지 경로

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic; // 게시글 주제 (FAQ, NOTICE)

    @CreationTimestamp
    private LocalDateTime createdAt; // 작성 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자

    public enum Topic {
        FAQ, NOTICE
    }

}