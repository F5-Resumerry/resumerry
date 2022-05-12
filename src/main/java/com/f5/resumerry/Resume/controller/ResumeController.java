package com.f5.resumerry.Resume.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Resume.dto.ResumeDTO;
import com.f5.resumerry.Resume.service.ResumeService;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/resume")
    @ApiOperation(value = "이력서 목록 조회")
    public ResponseEntity viewResumes(@ApiParam("유저 토큰") @RequestHeader String token,
                                      @ApiParam(value = "글 제목") @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                      @ApiParam(value = "직종(카테고리)") @RequestParam(name = "category",required = false, defaultValue = "ALL") String category,
                                      @ApiParam(value = "정렬기준") @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort,
                                      @ApiParam(value = "해시태그 기준") @RequestParam(name = "hashtag", required = false) String hashtag,
                                      @ApiParam(value = "연차기준") @RequestParam(name = "years", required = false, defaultValue = "0") Integer years) {
        //Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        //List<ResumeDTO> resumes = resumeService.viewResumes();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resume")
    @ApiOperation(value = "이력서 업로드")
    public ResponseEntity uploadResume() {
        // Todo. 이력서 업로드
        return ResponseEntity.ok().build();
    }

    @GetMapping("/resume/{user_id}")
    @ApiOperation(value = "마이 페이지에서 이력서 조회")
    public ResponseEntity viewResumesInMyPage(@ApiParam("유저 토큰") @RequestHeader String token,
                                              @ApiParam("요청 회원 번호") Long user_id) {
        Member member = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!user_id.equals(member.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }
        List<ResumeDTO> myResumes = resumeService.viewResumesInMyPage(user_id);
        return ResponseEntity.ok(myResumes);
    }





}
