package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.dto.BooleanResponseDTO;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@Slf4j
public class PostCommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/post/{member_id}/{post_id}/comment")
    @ApiOperation(value = "게시글 답변 등록")
    public ResponseEntity<BooleanResponseDTO> registerPostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "게시글 답변 DTO") @RequestBody GetCommentDTO getCommentDTO,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            postService.registerPostComment(memberIdByToken.getId(), postId, getCommentDTO);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @DeleteMapping("/post/{member_id}/{post_id}/comment/{comment_id}")
    @ApiOperation(value = "게시글 답변 삭제")
    public ResponseEntity<BooleanResponseDTO> deletePostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("삭제하기 위한 회원의 아이디가 같지 않습니다.");
        }
        try {
            postService.deletePostComment(memberId,postId,commentId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @GetMapping("/post/{member_id}/{post_id}/comment")
    @ApiOperation(value = "게시글 답변 조회")
    public ResponseEntity viewPostComments(
            @PathVariable("member_id") Long memberId,
            @PathVariable("post_id") Long postId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        JSONArray jsonArray = postService.viewComments(postId, memberIdByToken.getId());
        return ResponseEntity.status(HttpStatus.OK).body(jsonArray);
    }

    @PostMapping("/post/{member_id}/{post_id}/comment/{comment_id}/recommend")
    @ApiOperation(value = "추천 답변 등록")
    public ResponseEntity<BooleanResponseDTO> registerRecommendComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));

        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            postService.registerRecommendComment(memberIdByToken.getId(), postId, commentId);
        } catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }

    @PostMapping("/post/{user_id}/{post_id}/comment/{comment_id}/ban")
    @ApiOperation(value = "댓글 신고하기")
    public ResponseEntity<BooleanResponseDTO> banComment(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        Long reportMember = memberIdByToken.getId();
        BooleanResponseDTO booleanResponseDTO = new BooleanResponseDTO();
        try {
            postService.banComment(memberId,postId,commentId,reportMember);
        }  catch (Exception e) {
            booleanResponseDTO.setResult(false);
            return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
        }
        booleanResponseDTO.setResult(true);
        return ResponseEntity.status(HttpStatus.OK).body(booleanResponseDTO);
    }
}
