package com.spring_security.jwt.Dto;

import lombok.Getter;
import lombok.Setter;

/*
    7강 회원 가입 로직
    회원정보를 통해 인증 인가 작업을 진행하기 때문에 사용자로부터 회원 가입을 진행한 뒤 데이터베이스에 회원 정보를 저장
 */
@Getter @Setter
public class JoinDTO {

    private String username;
    private String password;
}
