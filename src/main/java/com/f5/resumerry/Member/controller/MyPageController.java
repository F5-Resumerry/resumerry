package com.f5.resumerry.Member.controller;

import com.f5.resumerry.Member.domain.dto.SignInDTO;
import com.f5.resumerry.Member.domain.dto.SignUpDTO;
import com.f5.resumerry.Member.domain.dto.ValidationGroups.ValidationSequence;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberServiceImpl;
import com.f5.resumerry.Member.service.MyPageService;
import com.f5.resumerry.Member.service.MyUserDetailsService;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.exception.AuthenticateException;
import com.f5.resumerry.exception.DuplicateException;
import com.f5.resumerry.exception.ErrorCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/my-page")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class MyPageController {

    private final MemberServiceImpl memberService;
    private final JwtUtil jwtUtil;
    private final MyPageService myPageService;

    @GetMapping("/{member_id}/clip")
    @ApiOperation(value = "마이페이지 스크랩 이력서 목록 조회")
    public ResponseEntity viewScrapList(@ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
                                        @ApiParam("member_id") @PathVariable Long member_id){

        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!member_id.equals(member.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myPageService.viewScrapInMyPage(member.getAccountName()));
    }

    @GetMapping("/{member_id}")
    @ApiOperation(value = "마이페이지 내 프로필 조회")
    public ResponseEntity viewProfile(@ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
                                        @ApiParam("member_id") @PathVariable Long member_id){

        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!member_id.equals(member.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }
        return ResponseEntity.status(HttpStatus.OK).body(myPageService.viewProfileInMyPage(member.getAccountName()));
    }



}