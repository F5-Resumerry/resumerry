package com.f5.resumerry.Resume.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Resume.repository.ResumeRecommendRepository;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.aws.AwsS3Service;
import com.f5.resumerry.dto.BooleanResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@Slf4j
@RequestMapping("/resume")
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

    @PostMapping("/{member_id}/{resume_id}/comment")
    @ApiOperation(value = "이력서 답변 등록")
    public ResponseEntity<BooleanResponseDTO> registerResumeComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "이력서 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "이력서 답변 DTO") @RequestBody GetCommentDTO getCommentDTO,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            resumeService.registerResumeComment(memberIdByToken.getId(), resumeId, getCommentDTO);
        }  catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @GetMapping("/{member_id}/{resume_id}/comment")
    @ApiOperation(value = "이력서 답변 조회")
    public ResponseEntity<?> viewResumeComments(
            @PathVariable("member_id") Long memberId,
            @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        JSONArray jsonArray = resumeService.viewComments(resumeId, memberIdByToken.getAccountName());
        return new ResponseEntity<>(jsonArray.toJSONString(), HttpStatus.OK);
    }

    @PutMapping("/{member_id}/{resume_id}/comment/{comment_id}")
    @ApiOperation(value = "이력서 답변 삭제")
    public ResponseEntity<BooleanResponseDTO> deleteResumeComments(
            @PathVariable("member_id") Long memberId,
            @PathVariable("resume_id") Long resumeId,
            @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            if (!resumeService.deleteResumeComment(memberIdByToken.getId(), commentId)){
                throw new RuntimeException("삭제할 권한이 없습니다");
            }
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PostMapping("/{member_id}/{resume_id}/comment/{comment_id}/recommend")
    @ApiOperation(value = "이력서 답변 추천")
    public ResponseEntity<BooleanResponseDTO> recommendResumeComment(
            @ApiParam(value = "member_id") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "resume_id") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "comment_id") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            resumeService.recommendResumeComment(memberIdByToken.getId(), commentId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PostMapping("/{member_id}/{resume_id}/comment/{comment_id}/bam")
    @ApiOperation(value = "이력서 답변 신고")
    public ResponseEntity<BooleanResponseDTO> reportResumeComment(
            @ApiParam(value = "member_id") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "resume_id") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "comment_id") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            resumeService.reportResumeComment(memberIdByToken.getId(), commentId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }
}