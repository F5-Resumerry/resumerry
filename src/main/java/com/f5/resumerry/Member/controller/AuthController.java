package com.f5.resumerry.Member.controller;

import com.f5.resumerry.Member.domain.dto.SignInDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.dto.ValidationGroups.ValidationSequence;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MyUserDetailsService;
import com.f5.resumerry.exception.AuthenticateException;
import com.f5.resumerry.exception.DuplicateException;
import com.f5.resumerry.exception.ErrorCode;
import com.f5.resumerry.Member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class AuthController {

    private final MemberServiceImpl memberServiceImpl;
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;


    @PostMapping("/sign-up")
    public ResponseEntity<Member> signUp(@Validated(ValidationSequence.class) @RequestBody SignUpDTO memberDTO) throws Exception {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/sign-in").toUriString());

        if (!memberServiceImpl.checkEmail(memberDTO.getEmail())) {
            throw new AuthenticateException("인증되지 않은 이메일입니다.");
        }

        if (memberServiceImpl.checkExistsEmail(memberDTO.getEmail())) {
            throw new DuplicateException("email", "email duplicated", ErrorCode.DUPLICATION);
        }

        if (memberServiceImpl.checkExistsAccountName(memberDTO.getAccountName())) {
            throw new DuplicateException("id", "id duplicated", ErrorCode.DUPLICATION);
        }

        if (memberServiceImpl.checkExistsNickname(memberDTO.getNickname())) {
            throw new DuplicateException("nickname", "nickname duplicated", ErrorCode.DUPLICATION);
        }

        return ResponseEntity.created(uri).body(memberServiceImpl.saveMember(memberDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody SignInDTO memberDTO) throws Exception {
        Map<String, String> result = new HashMap<>();
        AtomicBoolean check = new AtomicBoolean(false);
        try {
            if (memberServiceImpl.checkLogin(memberDTO.getAccountName(), memberDTO.getPassword()))
            {
                check.set(true);
            }
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }

        if(check.get()) {
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(memberDTO.getAccountName());
            final String jwt = jwtUtil.generateToken(userDetails);
            result.put("result", "SUCCESS");
            result.put("token", jwt);
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/test")
    public String test() {

        return "이메일 인증이 완료되었습니다";
    }



}