package com.f5.resumerry.security;

import com.f5.resumerry.Member.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;

// header에 있는 인증 토큰을 처리하기 위해 따로 빼놓았습니다. 각 컨트롤러에서 헤더를 받고 해당 메서드로 userid를 받아 사용자의 id를 추출할 것 입니다.
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    public String Token2Username(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return username;
    }
}
