package com.spring_security.jwt.Controller;

import com.spring_security.jwt.Dto.JoinDTO;
import com.spring_security.jwt.Service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/*
    7강 회원 가입 로직
    회원정보를 통해 인증 인가 작업을 진행하기 때문에 사용자로부터 회원 가입을 진행한 뒤 데이터베이스에 회원 정보를 저장
 */
@Slf4j
@Controller
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/join")
    public String joinP() {

        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        log.info("username - {}", joinDTO.getUsername());
        joinService.joinProcess(joinDTO);

        return "redirect:/login";
    }
}
