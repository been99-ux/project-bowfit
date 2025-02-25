package com.example.finaln.dto;

import com.example.finaln.entity.QnaPost;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaPostDto {

    private Long id;
    private String writerName;
    private String title;
    private String content;
    private MultipartFile fileInput; // 이미지 파일 업로드를 위한 필드
    private boolean isSecret;
    private QnaPost.Status status;
    private LocalDateTime createdAt;
    private String imageUrl; // 🛠 추가된 필드 (DB에 저장된 파일 경로)
    // member 에서 가져옴
    private String loginId;
    private String password;

    public boolean isSecret() {
        return isSecret;
    }

    // ✅ QnaPost -> QnaPostDto 변환 메서드
    public static QnaPostDto fromEntity(QnaPost qnaPost) {
        QnaPostDto dto = new QnaPostDto();
        dto.setId(qnaPost.getId());
        dto.setTitle(qnaPost.getTitle());
        dto.setContent(qnaPost.getContent());
        dto.setWriterName(qnaPost.getMember().getName());  // 작성자 정보
        dto.setCreatedAt(qnaPost.getCreatedAt());
        dto.setImageUrl(qnaPost.getImageUrl());
        return dto;
    }

}
