package com.f5.resumerry.Resume.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Resume.repository.ResumeRecommendRepository;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.aws.AwsS3Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@Slf4j
public class ResumeCommentController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final AwsS3Service awsS3Service;
    private final ResumeRecommendRepository resumeRecommendRepository;

    public ResumeCommentController(ResumeService resumeService, MemberService memberService, JwtUtil jwtUtil, AwsS3Service awsS3Service, ResumeRecommendRepository resumeRecommendRepository) {
        this.resumeService = resumeService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.awsS3Service = awsS3Service;
        this.resumeRecommendRepository = resumeRecommendRepository;
    }

    @GetMapping("/resume/{member_id}/{resume_id}/comment")
    @ApiOperation(value = "이력서 답변 조회")
    public ResponseEntity<?> viewResumeComments(
            @PathVariable("member_id") Long memberId,
            @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        JSONArray jsonArray = resumeService.viewComments(resumeId, memberIdByToken.getAccountName());
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }
}