package com.example.finaln.repository;

import com.example.finaln.entity.FaqNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FaqNoticeRepository extends JpaRepository<FaqNotice, Long> {
    List<FaqNotice> findTop5ByTopicOrderByCreatedAtDesc(FaqNotice.Topic topic, Pageable pageable);
    List<FaqNotice> findByTopic(FaqNotice.Topic topic); // 특정 topic에 해당하는 게시글 찾기z
    Page<FaqNotice> getPagedPostsByTopic(FaqNotice.Topic topic, Pageable pageable);

    @Query("SELECT f FROM FaqNotice f WHERE " +
            "(LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(f.member.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<FaqNotice> searchPosts(@Param("query") String query, Pageable pageable);

    @Query("SELECT f FROM FaqNotice f WHERE f.topic = :topic AND " +
            "(LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(f.member.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<FaqNotice> searchPosts(@Param("topic") FaqNotice.Topic topic,
                                @Param("query") String query,
                                Pageable pageable);

    // ✅ 단일 게시글 조회
    Optional<FaqNotice> findById(Long id);

}
