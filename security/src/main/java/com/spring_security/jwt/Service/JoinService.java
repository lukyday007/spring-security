package com.spring_security.jwt.Service;

import com.spring_security.jwt.Dto.JoinDTO;
import com.spring_security.jwt.Entity.UserEntity;
import com.spring_security.jwt.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/*
    7강 회원 가입 로직
    회원정보를 통해 인증 인가 작업을 진행하기 때문에 사용자로부터 회원 가입을 진행한 뒤 데이터베이스에 회원 정보를 저장
 */
@Slf4j
@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    // Autowired 어노테이션 사용으로 BCryptPasswordEncoder 객체를 자동으로 주입
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDTO joinDTO) {

        // db에 이미 동일한 유저 네임을 가진 회원이 존재하는지?
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        log.info("isUSer - {}", isUser);
        if (isUser)
            return;

        UserEntity data = new UserEntity();
        log.info("username - {}", joinDTO.getUsername());
        data.setUsername(joinDTO.getUsername());

        // 사용자의 비밀번호를 BCrypt 알고리즘으로 암호화 (단방향 해싱 방식)
        // 왜 BCrypt 사용?
        //      1. 단방향 암호화 : 복호화가 불가능하여 안전
        //      2. Salt 자동 추가 : 같은 비밀번호라도 다른 해시 값 생성
        //      3. 반복연산 설정 가능 : 공격에 대한 저항력을 조절
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("USER");

        userRepository.save(data);
    }
}
