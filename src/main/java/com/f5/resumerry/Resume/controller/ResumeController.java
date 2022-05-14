package com.f5.resumerry.Resume.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Resume.dto.*;
import com.f5.resumerry.Resume.repository.ResumeRecommendRepository;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.aws.AwsS3Service;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RestController
public class ResumeController {
    private final ResumeService resumeService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final AwsS3Service awsS3Service;
    private final ResumeRecommendRepository resumeRecommendRepository;

    public ResumeController(ResumeService resumeService, MemberService memberService, JwtUtil jwtUtil, AwsS3Service awsS3Service, ResumeRecommendRepository resumeRecommendRepository) {
        this.resumeService = resumeService;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.awsS3Service = awsS3Service;
        this.resumeRecommendRepository = resumeRecommendRepository;
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

        resumeService.uploadResume(member.getId(), fullFileLink, uploadResumeDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resume/{user_id}")
    @ApiOperation(value = "마이 페이지에서 이력서 조회")
    public ResponseEntity viewResumesInMyPage(@ApiParam("유저 토큰") @RequestHeader String token,
                                              @ApiParam("요청 회원 번호")@PathVariable Long user_id) {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!user_id.equals(member.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }
        List<ResumeDTO> myResumes = resumeService.viewResumesInMyPage(user_id);
        return ResponseEntity.ok(myResumes);
    }

    @GetMapping(value = "/resume/{user_id}/{resume_id}")
    @ApiOperation(value = "이력서 세부조회")
    public ResponseEntity viewResume(@ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token,
                                   @ApiParam(value = "이력서 주인 아이디") @PathVariable("user_id") Long userId,
                                   @ApiParam(value = "이력서 아이디") @PathVariable("resume_id") Long resumeId) {
        System.out.println("print");
        Long tokenId = memberService.getMember(jwtUtil.extractUsername(token.substring(7))).getId();
        System.out.println(tokenId);
        ViewResumeDTO resumeResponse = resumeService.viewResume(userId,resumeId,tokenId);
        return ResponseEntity.ok(resumeResponse);
    }

    @DeleteMapping("/resume/{user_id}/{resume_id}")
    @ApiOperation(value = "이력서 삭제")
    public ResponseEntity deleteResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "이력서 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));

        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }

        Map<String, Boolean> param = new HashMap<>();
        try {
            resumeService.deleteResume(memberIdByToken.getId(),resumeId);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @PostMapping("/resume/{user_id}/{resume_id}")
    @ApiOperation(value = "이력서 추천")
    public ResponseEntity recommendResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "이력서 토큰") @RequestHeader("Authorization") String token) {
        Map<String, Boolean> param = new HashMap<>();
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        try {
            resumeService.recommendResume(memberIdByToken.getAccountName(), resumeId);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok().body(param);
    }

    @PostMapping("/resume/{user_id}/{resume_id}/scrap")
    @ApiOperation(value = "이력서 스크랩")
    public ResponseEntity scrapResume(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("resume_id") Long resumeId,
            @ApiParam(value = "이력서 토큰") @RequestHeader("Authorization") String token) {
        Map<String, Boolean> param = new HashMap<>();
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        try {
            resumeService.scrapResume(memberIdByToken.getAccountName(), resumeId);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok().body(param);
    }

    @PutMapping(value = "/resume/{user_id}/{resume_id}")
    @ApiOperation("이력서 수정")
    public ResponseEntity putResume(@ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token,
                                    @ApiParam("이력서 변경사항들") @ModelAttribute UploadResumeDTO uploadResumeDTO,
                                    @ApiParam(value = "이력서 주인 아이디") @PathVariable(name = "user_id") Long userId,
                                    @ApiParam(value = "이력서 아이디") @PathVariable(name = "resume_id") Long resumeId,
                                    @ApiParam("이력서 파일") @RequestPart(required = false) MultipartFile file) throws IOException
    {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));


        LocalDate date = LocalDate.now();
        String filePath = String.valueOf(date.getYear()) + "/" + member.getNickname();

        String fullFileLink = "/" + filePath + "/" + file.getOriginalFilename();


        if (!userId.equals(member.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }

        resumeService.updateResume(member.getId(), resumeId, uploadResumeDTO, fullFileLink);
        awsS3Service.upload(file, filePath);

        return ResponseEntity.ok().build();
    }
}
