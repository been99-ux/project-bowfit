package com.example.finaln.repository;

import com.example.finaln.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Page<Member> findByRole(Member.Role role, Pageable pageable);  // 페이징 처리 추가
    Page<Member> findByNameContaining(String searchKeyword, Pageable pageable);
}
