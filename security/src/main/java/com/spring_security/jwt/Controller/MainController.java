package com.spring_security.jwt.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

/*
    9. 세션 정보
 */

@Controller
public class MainController {

    @GetMapping("/")
    public String mainP(Model model) {

        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        // SecurityContextHolder.getContext().getAuthentication()
        // 스프링 시큐리티가 관리하는 현재 인증 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // authentication.getAuthorities() : 로그인한 사용자의 권한 정보를 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        model.addAttribute("id", id);
        model.addAttribute("role", role);

        return "main";
    }

}
