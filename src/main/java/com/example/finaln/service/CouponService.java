package com.example.finaln.service;

import com.example.finaln.entity.Coupon;
import com.example.finaln.entity.Member;
import com.example.finaln.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final Logger logger = LoggerFactory.getLogger(CouponService.class);

    // 회원가입 시 쿠폰 지급
    @Transactional
    public void issueCouponsToNewUser(Member member) {
        if (member.getRole() == Member.Role.USER) {
            couponRepository.save(new Coupon("3만원 이상 3천원 할인", 30000, 3000, member));
            couponRepository.save(new Coupon("5만원 이상 5천원 할인", 50000, 5000, member));
            couponRepository.save(new Coupon("10만원 이상 1만원 할인", 100000, 10000, member));
        }
    }

    @Transactional(readOnly = true)
    public int getCouponCountByMember(Long memberId) {
        int count = couponRepository.countByMemberId(memberId);
        logger.info("쿠폰 개수 조회 - memberId: {}, 개수: {}", memberId, count);
        return count;
    }

    public List<Coupon> getCouponsByMemberId(Long memberId) {
        return couponRepository.findByMember_Id(memberId); // ✅ 해당 회원의 쿠폰 목록 조회
    }


}
