package com.example.finaln.repository;

import com.example.finaln.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    int countByMemberId(Long memberId);
    List<Coupon> findByMember_Id(Long memberId);
}
