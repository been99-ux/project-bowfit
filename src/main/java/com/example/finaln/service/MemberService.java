package com.example.finaln.service;

import com.example.finaln.dto.MemberUpdateDto;
import com.example.finaln.entity.Member;
import com.example.finaln.entity.MemberDetail;
import com.example.finaln.repository.MemberDetailRepository;
import com.example.finaln.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository; // 멤버 정보
    private final MemberDetailRepository memberDetailRepository; // 멤버 상세정보
    private final CouponService couponService; // 회원가입 쿠폰

    @Transactional
    public Member registerUser(Member member) {

        // 기본 권한 USER 설정
        member.setRole(Member.Role.USER);

        // 회원 저장
        Member savedMember = memberRepository.save(member);

        // 쿠폰 지급
        couponService.issueCouponsToNewUser(savedMember);

        return savedMember;
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        member.setLoginId(memberUpdateDto.getLoginId());
        member.setPassword(memberUpdateDto.getPassword());
        member.setName(memberUpdateDto.getName());
        member.setEmail(memberUpdateDto.getEmail());
        member.setPhone(memberUpdateDto.getPhone());
        System.out.println(memberUpdateDto.getLoginId());
        System.out.println(memberUpdateDto.getPassword());

        MemberDetail memberDetail = member.getMemberDetail();
        if (memberDetail == null) {
            memberDetail = new MemberDetail();
            memberDetail.setMember(member);
        }

        memberDetail.setPostcode(memberUpdateDto.getPostcode());
        memberDetail.setAddress1(memberUpdateDto.getAddress1());
        memberDetail.setAddress2(memberUpdateDto.getAddress2());
        System.out.println(memberUpdateDto.getPostcode());
        System.out.println(memberUpdateDto.getAddress1());
        System.out.println(memberUpdateDto.getAddress2());

        member.setMemberDetail(memberDetail);
        memberRepository.save(member);
    }

    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean isPhoneExists(String phone) {
        return memberRepository.existsByPhone(phone);
    }

    // 회원 탈퇴
    public void withdrawMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    // 관리자 페이지 회원 목록 페이징
    public Page<Member> getUserMembers(Pageable pageable) {
        return memberRepository.findByRole(Member.Role.USER, pageable);
    }

    // 관리자페이지 회원 검색
    public Page<Member> memberSearchList(String searchKeyword, Pageable pageable) {
        return memberRepository.findByNameContaining(searchKeyword, pageable);
    }

    public boolean isLoginIdDuplicate(String loginId) {
        return memberRepository.findByLoginId(loginId) != null;
    }

    public Member getMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }
}