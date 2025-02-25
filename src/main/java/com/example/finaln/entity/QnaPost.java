package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    private String imageUrl; // 첨부 이미지 URL

    public enum Status {
        PENDING, // 답변대기중
        ANSWERED // 답변완료
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // 답변 상태 (기본: 답변대기중)

    @Column(nullable = false)
    private boolean isSecret; // 비밀글 여부

    private String password; // 비밀글 비밀번호 (비밀글일 경우 저장됨)

    @CreationTimestamp
    private LocalDateTime createdAt; // 작성 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자 (USER)

    @OneToOne(mappedBy = "qnaPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private QnaAnswer qnaAnswer; // 답변 (ADMIN이 작성)

}
