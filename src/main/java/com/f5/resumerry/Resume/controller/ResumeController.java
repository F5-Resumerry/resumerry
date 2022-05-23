package com.f5.resumerry.Resume.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.aws.AwsS3Service;
import com.f5.resumerry.dto.BooleanResponseDTO;
import com.f5.resumerry.exception.AuthenticateException;
import com.querydsl.core.Tuple;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.json.simple.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Controller
@RestController
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final AwsS3Service awsS3Service;


    public ResumeController(ResumeService resumeService, MemberService memberService, JwtUtil jwtUtil, AwsS3Service awsS3Service) {
        this.resumeService = resumeService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.awsS3Service = awsS3Service;

    }

    @GetMapping("/resume")
    @ApiOperation(value = "이력서 목록 조회")
    public ResponseEntity viewResumes(@ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
                                      @ModelAttribute ResumeFilterDTO resumeFilterDTO) {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        List<FilterViewResumeDTO> resumes = resumeService.viewResumes(resumeFilterDTO, member.getId());
        return ResponseEntity.ok(resumes);
    }

    @PostMapping(value = "/resume", consumes = {"multipart/form-data", MediaType.MULTIPART_FORM_DATA_VALUE })
    @ApiOperation(value = "이력서 업로드")
    public ResponseEntity uploadResume(@ApiParam("유저 토큰") @RequestHeader(value = "Authorization") String token,
                                       @ModelAttribute UploadResumeDTO uploadResumeDTO,
                                       @ApiParam("해시태그")@RequestParam List<String> hashtagList,
                                       @ApiParam("이력서 파일") @RequestPart(required = false) MultipartFile file) throws IOException {
        // Todo. 이력서 업로드
        // 1. s3에 링크 업로드
        // 2. 업로드 위치 가져와서 repo를 통해 업로드
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));

        LocalDate date = LocalDate.now();
        String filePath = String.valueOf(date.getYear()) + "/" + member.getNickname();

        // 해당 filePath에 파일 업로드
        awsS3Service.upload(file, filePath);

        String fullFileLink = "/" +filePath + "/" + file.getOriginalFilename();


        resumeService.uploadResume(member.getId(), fullFileLink, uploadResumeDTO, hashtagList);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resume/{user_id}")
    @ApiOperation(value = "마이 페이지에서 이력서 조회")
    public ResponseEntity<?> viewResumesInMyPage(@ApiParam("유저 토큰") @RequestHeader String token,
                                              @ApiParam("요청 회원 번호")@PathVariable Long user_id) {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!user_id.equals(member.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }
        JSONArray result = new JSONArray();
        result = resumeService.viewResumesInMyPage(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping(value = "/resume/{user_id}/{resume_id}")
    @ApiOperation(value = "이력서 세부조회")
    public ResponseEntity viewResume(@ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token,
                                   @ApiParam(value = "이력서 주인 아이디") @PathVariable("user_id") Long userId,
                                   @ApiParam(value = "이력서 아이디") @PathVariable("resume_id") Long resumeId) {
        Long tokenId = memberService.getMember(jwtUtil.extractUsername(token.substring(7))).getId();
        ViewResumeDTO resumeResponse = resumeService.viewResume(userId,resumeId,tokenId);
        return ResponseEntity.ok(resumeResponse);
    }

    @DeleteMapping("/resume/{user_id}/{resume_id}")
    @ApiOperation(value = "이력서 삭제")
    public ResponseEntity<BooleanResponseDTO> deleteResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "이력서 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }
        try {
            resumeService.deleteResume(memberIdByToken.getId(),resumeId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PostMapping("/resume/{user_id}/{resume_id}/recommend")
    @ApiOperation(value = "이력서 추천")
    public ResponseEntity<BooleanResponseDTO> recommendResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "이력서 토큰") @RequestHeader("Authorization") String token) {
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        try {
            resumeService.recommendResume(memberIdByToken.getId(), resumeId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PostMapping("/resume/{user_id}/{resume_id}/scrap")
    @ApiOperation(value = "이력서 스크랩")
    public ResponseEntity<BooleanResponseDTO> scrapResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "이력서 토큰") @RequestHeader("Authorization") String token) {
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        try {
            resumeService.scrapResume(memberIdByToken.getId(), resumeId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PutMapping(value = "/resume/{user_id}/{resume_id}")
    @ApiOperation("이력서 수정")
    public ResponseEntity putResume(@ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token,
                                    @ApiParam("이력서 변경사항들") @ModelAttribute UploadResumeDTO uploadResumeDTO,
                                    @ApiParam(value = "이력서 주인 아이디") @PathVariable(name = "user_id") Long userId,
                                    @ApiParam(value = "이력서 아이디") @PathVariable(name = "resume_id") Long resumeId,
                                    @ApiParam("해시태그")@RequestParam List<String> hashtagList,
                                    @ApiParam("이력서 파일") @RequestPart(required = false) MultipartFile file) throws IOException
    {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));


        LocalDate date = LocalDate.now();
        String filePath = String.valueOf(date.getYear()) + "/" + member.getNickname();

        String fullFileLink = "/" + filePath + "/" + file.getOriginalFilename();


        if (!userId.equals(member.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }

        resumeService.updateResume(member.getId(), resumeId, uploadResumeDTO, fullFileLink, hashtagList);
        awsS3Service.upload(file, filePath);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/resume/{member_id}/{resume_id}/unlock")
    @ApiOperation("이력서 잠금 해제")
    public ResponseEntity unLockResume(@ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token,
                                         @ApiParam(value = "보낸 유저 아이디") @PathVariable("member_id") Long memberId,
                                        @ApiParam(value = "이력서 아이디") @PathVariable("resume_id") Long resumeId) {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if (!memberId.equals(member.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }
        return ResponseEntity.ok(resumeService.unLockResume(member.getId(), resumeId));
    }

    @GetMapping("/recommend/{user_id}/{resume_id}")
    @ApiOperation(value = "TF-IDF 추천이력서 조회")
    public ResponseEntity<List<ResumeRecommendDTO>> getTokenHistory(
            @ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
            @ApiParam("유저 번호") @PathVariable Long user_id,
            @ApiParam("이력서 번호") @PathVariable Long resume_id
    ) {
        return ResponseEntity.ok(resumeService.getAllResumeRecommend(user_id, resume_id));
    }

}
