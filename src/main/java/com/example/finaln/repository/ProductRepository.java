package com.example.finaln.repository;

import com.example.finaln.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 카테고리로 상품 조회
    List<Product> findByCategory(String category);
}
