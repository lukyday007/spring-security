package com.spring_security.jwt.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*
    7강 회원 가입 로직
    회원정보를 통해 인증 인가 작업을 진행하기 때문에 사용자로부터 회원 가입을 진행한 뒤 데이터베이스에 회원 정보를 저장
 */
@Entity
@Setter @Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
        회원 중복 검증
        username에 대해서 중복된 가입이 발생하면 서비스에서 아주 치명적인 문제가 발생하기 때문에 백엔드단에서 중복 검증과 중복 방지 로직을 작성
     */
    @Column(unique = true)
    private String username;
    private String password;

    private String role;

}