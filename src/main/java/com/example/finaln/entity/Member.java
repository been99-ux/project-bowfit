package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId; // 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name; // 이름

    @Column(unique = true, nullable = false)
    private String email; // 이메일

    @Column(unique = true, nullable = false)
    private String phone; // 전화번호

    @CreationTimestamp // 자동으로 현재 시간 저장
    private LocalDateTime createdAt; // 회원 생성 날짜/시간

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();

    @Enumerated(EnumType.STRING) // Enum으로 역할 구분
    private Role role; // USER

    public enum Role {
        USER, ADMIN
    }

    @Column(nullable = false)
    private boolean isDeleted; // 탈퇴 여부 (기본값: false)

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberDetail memberDetail; // MemberDetail 엔티티와의 관계

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaqNotice> faqNotices = new ArrayList<>(); // FaqNotice 엔티티와의 관계

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaPost> qnaPosts = new ArrayList<>();


}
