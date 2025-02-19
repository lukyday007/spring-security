package com.spring_security.jwt.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
    스프링 시큐리티 3강

    [인가]
    특정한 경로에 요청이 오면 Controller 클래스에 도달하기 전 필터에서 Spring Security가 검증을 함
        1. 해당 경로의 접근은 누구에게 열려 있는지
        2. 로그인이 완료된 사용자인지
        3. 해당되는 role을 가지고 있는지

    Security Configuration
        인가 설정을 진행하는 클래스 -> SecurityFilterChain 설정을 진행함

 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
        5강 BCrypt 암호화 메서드

        스프링 시큐리티는 사용자 인증(로그인)시 비밀번호에 대해 단방향 해시 암호화를 진행하여 저장되어 있는 비밀번호와 대조
        따라서 회원가입시 비밀번호 항목에 대해서 암호화를 진행해야 함
        스프링 시큐리티는 암호화를 위해 BCrypt Password Encoder를 제공하고 권장함
        따라서 해당 클래스를 return 하는 메소드를 만들어 @Bean으로 등록하여 사용하면 됨
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 예외 설정 추가
        // POST /joinProc 요청 시 403 Forbidden -> Spring Security가 CSRF 보호를 활성화한 상태에서 CSRF 토큰이 전달되지 않았기 때문
        http
                // ✅ CSRF 비활성화 (joinProc, loginProc 예외 처리)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/joinProc", "/loginProc") // ✅ 특정 요청에서 CSRF 비활성화
                );

        // 경로별 인가 작업 커스텀
        // authorizeHttpRequests : HTTP 요청에 대한 접근 권한을 설정하는 메서드
        http.authorizeHttpRequests((auth) -> auth
                // requestMatchers : 특정 URL 패턴에 대한 접근 정책을 정의
                //      permitAll : 모든 사용자(로그인 여부 관계없이) 해당 URL에 접근할 수 있도록 허용
                .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                //      hasRole : 특정 역할만 가진 사용자만 URL에 접근 가능하도록 설정
                .requestMatchers("/admin").hasAuthority("ADMIN")

                /*
                    3강 Security Config 클래스
                 */
                //      hasAnyRole : 여러 개의 역할을 허용할 때 사용
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                // 위에서 설정한 URL 패턴 외의 모든 요청은 인증된 사용자만 접근 가능
                .anyRequest().authenticated()
        );

        /*
            4강 커스텀 로그인
         */
        // formLogin : Spring Security 에서 기본 로그인 방식(Form Login)을 활성화
        http.formLogin((auth) -> auth
                // loginPage : 사용자가 로그인 페이지에 접근할 때, 기본 로그인 페이지 대신 '사용자 정의 로그인 페이지' /login을 사용
                .loginPage("/login")
                // loginProcessingUrl : 로그인 폼을 제출할 때, 로그인 요청을 처리할 엔드포인트 지정
                //      사용자가 /login에서 아이디/비번 입력 후 제출하면 /loginProc로 POST 요청 전송
                //      Spring Security가 내부적으로 /loginProc을 가로채서 로그인 인증 수행
                .loginProcessingUrl("/loginProc")
                // permitAll : /login, /loginProc은 모든 사용자(비인증 사용자 포함)가 접근 가능하도록 설정
                .permitAll()
        );

        /*
            11강. CSRF 설정

            [CSRF란?] : Cross-Site Request Forgery 는 요청을 위조하여 사용자가 원하지 않아도 서버 측으로 특정 요청을 강제로 보내는 방식 (회원 정보 변경, 게시글 CRUD를 사용자 모르게 요청)

            [개발 환경에서 csrf disable()] : 개발 환경에서는 Security Config 클래스를 통해 csrf 설정을 disable 설정. "배포 환경"에서는 공격방지를 위해 disable설정을 제거하고 추가적인 설정 진행 필요
         */
//        http.csrf((auth) -> auth.disable());  // 주석 처리 시 로그인 불가

        /*
            10강. 세션 설정
                [다중 로그인 설정] : 동일한 아이디로 다중 로그인을 진행할 경우에 대한 설정 방법은 세션 통제를 통해 진행

                maximumSessions (정수) : 하나의 아이디에 대한 다중 로그인 허용 개수
                maxSessionPreventsLogin (불린) : 다중 로그인 개수를 초과하였을 경우 처리 방법
                    - true : 초과시 새로운 로그인 차단
                    - false : 초과시 기존 세션 하나 삭제
         */
         http.sessionManagement((auth) -> auth
                 .maximumSessions(1)
                 .maxSessionsPreventsLogin(true));

         /*
                [세션 고정 보호] : 세션 고정 공격을 보호하기 위한 로그인 성공시 세션 설정 방법은 sessionManagement() 메소드의 sessionFixation() 메소드를 통해서 설정

                .sessionFixation().none()
                로그인 시 세션 정보 변경 안함 -> 보호 안됨 -> 쿠키 탈취 시 해커가 동일한 권한 수행 가능
                .sessionFixation().newSession()
                로그인 시 세션 새로 생성
          */
        http.sessionManagement((auth) -> auth
                // 로그인 시 동일한 세션에 대한 id 변경 => 자주 사용
                .sessionFixation().changeSessionId()
        );

        // 세션 설정
        /*
            JWT를 통한 인증/인가를 위해서 세션을 STATELESS 상태로 설정해야 함
        */
//        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();    // 받아온 시큐리티 인자를 http 로 리턴
    }
}
