package com.f5.resumerry.Member.controller;

import com.f5.resumerry.Member.domain.dto.AmendRequestDTO;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberServiceImpl;
import com.f5.resumerry.Member.service.MyPageService;
import com.f5.resumerry.Reward.TokenHistory;
import com.f5.resumerry.aws.AwsS3Service;
import com.f5.resumerry.dto.BooleanResponseDTO;
import com.f5.resumerry.exception.AuthenticateException;
import com.f5.resumerry.security.AuthService;
import com.f5.resumerry.selector.AwsUpload;
import com.f5.resumerry.selector.CategoryEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/my-page")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class MyPageController {

    private final MemberServiceImpl memberService;
    private final JwtUtil jwtUtil;
    private final MyPageService myPageService;

    private BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO(true);

    private final AuthService authService;



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

    @GetMapping("/token/{user_id}")
    @ApiOperation(value = "내 토큰 사용 이력 조회")
    public ResponseEntity<List<TokenHistory>> getTokenHistory(
            @ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
            @ApiParam("유저 번호") @PathVariable Long user_id
    ) {
        return ResponseEntity.ok(memberService.getAllTokenHistory(user_id));
    }

    @PutMapping("/{member_id}")
    @ApiOperation(value = "마이페이지 내 정보 수정")
    public  ResponseEntity amendMemberInfo(@ApiParam("유저 토큰") @RequestHeader("Authorization") String token,
                                         @ModelAttribute AmendRequestDTO amendRequestDTO,
                                         @ApiParam("이미지 소스") @RequestPart(value = "imageSrc", required = false) MultipartFile file,
                                         @ApiParam("멤버 아이디") @PathVariable("member_id") Long memberId) {


        Member authMember = authService.Token2Member(token);
        LocalDate date = LocalDate.now();
        String imageSrc = String.valueOf(date.getYear()) + "/" + authMember.getNickname();
        String fullImageSrc =  "/" + imageSrc + "/" + file.getOriginalFilename();

        if(!memberId.equals(authMember.getId())) {
            throw new AuthenticateException("잘못된 회원 입니다");
        }

        try {
            memberService.amendMemberInfo(memberId, amendRequestDTO ,fullImageSrc, file, imageSrc, AwsUpload.IMAGE);
        }catch (Exception e) {
            booleanResponseDTO.setResult(false);
        }

        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

}