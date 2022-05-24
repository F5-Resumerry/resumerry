package com.f5.resumerry.security;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MemberService memberService;

    public Member Token2Member(String token) {
        return userName2Member(Token2Username(token));
    }
    private String Token2Username(String token) {
        return jwtUtil.extractUsername(token.substring(7));
    }
    private Member userName2Member(String userName) {
        return memberService.getMember(userName);
    }
}
