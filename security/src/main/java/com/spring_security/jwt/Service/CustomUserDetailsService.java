package com.spring_security.jwt.Service;

import com.spring_security.jwt.Dto.CustomUserDetails;
import com.spring_security.jwt.Entity.UserEntity;
import com.spring_security.jwt.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
    8강 인증
        시큐리티를 통해 인증을 진행하는 방법은 사용자가 Login 페이지를 통해 아이디, 비밀번호 POST 요청시
        스프링 시큐리티가 데이터 베이스에 저장된 회원 정보를 조회 후 비밀 번호를 검증하고 서버 세션 저장소에 해당 아이디에 대산 세션을 저장
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        // 조회된 UserEntity 객체를 CustomUserDetails로 감싸서 반환
        if (userData != null)
            return new CustomUserDetails(userData);

        return null;
    }
}
