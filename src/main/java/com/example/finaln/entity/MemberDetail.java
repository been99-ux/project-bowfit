package com.example.finaln.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postcode; // 우편번호

    @Column(nullable = false)
    private String address1; // 주소

    @Column(nullable = false)
    private String address2; // 상세주소

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // Member 엔티티와의 관계

}
