package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    private String cname; // 쿠폰명
    private int cminPurchaseAmount; // 최소 구매 금액
    private int cdiscountAmount; // 할인 금액
    private LocalDateTime cissuedAt; // 발급일
    private LocalDateTime cexpiryDate; // 만료일 (발급일로부터 1년)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Coupon(String cname, int cminPurchaseAmount, int cdiscountAmount, Member member) {
        this.cname = cname;
        this.cminPurchaseAmount = cminPurchaseAmount;
        this.cdiscountAmount = cdiscountAmount;
        this.cissuedAt = LocalDateTime.now();
        this.cexpiryDate = cissuedAt.plusYears(1); // 1년 후 만료
        this.member = member;
    }
}
