package com.example.finaln.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String postcode;
    private String address1;
    private String address2;
}
