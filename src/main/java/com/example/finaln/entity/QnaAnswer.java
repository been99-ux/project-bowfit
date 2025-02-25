package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "qna_post_id", nullable = false)
    private QnaPost qnaPost; // 관련된 QnaPost

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 답변 내용

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 답변 작성자 (ADMIN)

    private LocalDateTime createdAt; // 답변 작성 시간

    @PrePersist


    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }



}
