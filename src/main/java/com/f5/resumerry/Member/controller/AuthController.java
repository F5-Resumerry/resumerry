package com.f5.resumerry.Member.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.f5.resumerry.Member.dto.SignInDTO;
import com.f5.resumerry.Member.dto.SignUpDTO;
import com.f5.resumerry.Member.dto.ValidationGroups.ValidationSequence;
import com.f5.resumerry.Member.entity.Member;
import com.f5.resumerry.Member.exception.AuthenticateException;
import com.f5.resumerry.Member.exception.DuplicateException;
import com.f5.resumerry.Member.exception.ErrorCode;
import com.f5.resumerry.Member.service.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberServiceImpl memberServiceImpl;

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

        if (!memberServiceImpl.checkExistsNickname(memberDTO.getNickname())) {
            throw new DuplicateException("nickname", "nickname duplicated", ErrorCode.DUPLICATION);
        }

        return ResponseEntity.created(uri).body(memberServiceImpl.saveMember(memberDTO));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Map<String, Boolean>> signIn(@Validated(ValidationSequence.class) @RequestBody SignInDTO memberDTO) throws Exception {
        Map<String, Boolean> result = new HashMap<>();
        result.put("access token", true);
        result.put("refresh token", true);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String accountName = decodedJWT.getSubject();
                Member member = memberServiceImpl.getMember(accountName);
                String access_token = JWT.create()
                        .withSubject(member.getAccountName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}