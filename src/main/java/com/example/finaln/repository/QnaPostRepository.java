package com.example.finaln.repository;

import com.example.finaln.entity.QnaPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    Page<QnaPost> findAll(Pageable pageable);
}
