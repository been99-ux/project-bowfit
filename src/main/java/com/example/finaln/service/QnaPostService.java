package com.example.finaln.service;

import com.example.finaln.dto.QnaPostDto;
import com.example.finaln.entity.QnaPost;
import com.example.finaln.repository.QnaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QnaPostService {

    private final QnaPostRepository qnaPostRepository;

    // 최신 Q&A 게시물 5개 가져오기
    public List<QnaPostDto> getLatestQnaPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return qnaPostRepository.findAll(pageable).stream()
                .map(QnaPostDto::fromEntity) // Entity -> DTO 변환
                .collect(Collectors.toList());
    }

    // 모든 Q&A 게시글 조회
    public List<QnaPostDto> getAllQnaPosts() {
        return qnaPostRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(QnaPostDto::fromEntity)
                .collect(Collectors.toList());
    }


    // 파일 업로드 처리 및 QnaPost 저장
    public QnaPost createQnaPost(QnaPost qnaPost, MultipartFile fileInput) throws IOException {
        if (fileInput != null && !fileInput.isEmpty()) {
            String uploadDir = "C:/upload/";  // 저장할 폴더 지정
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                boolean isCreated = dir.mkdirs();  // 폴더가 없으면 생성
                System.out.println("📂 업로드 폴더 생성 여부: " + isCreated);
            }

            String originalFilename = fileInput.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;  // 파일명 중복 방지
            String filePath = uploadDir + storedFilename;

            File destFile = new File(filePath);
            fileInput.transferTo(destFile);  // 파일 저장

            System.out.println("✅ 파일 업로드 성공: " + filePath);

            qnaPost.setImageUrl("/upload/" + storedFilename);  // DB 저장 경로
        } else {
            qnaPost.setImageUrl(null);  // 파일이 없을 경우 NULL 저장
            System.out.println("⚠ 파일이 선택되지 않음.");
        }

        return qnaPostRepository.save(qnaPost); // 🛠 저장 후, 저장된 QnaPost 반환
    }

}
