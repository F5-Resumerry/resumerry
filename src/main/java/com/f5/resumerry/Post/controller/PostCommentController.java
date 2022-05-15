package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
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
    public ResponseEntity registerPostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "게시글 답변 DTO") @RequestBody GetCommentDTO getCommentDTO,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.registerPostComment(memberIdByToken.getId(), postId, getCommentDTO);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @DeleteMapping("/post/{member_id}/{post_id}/comment/{comment_id}")
    @ApiOperation(value = "게시글 답변 삭제")
    public ResponseEntity deletePostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("삭제하기 위한 회원의 아이디가 같지 않습니다.");
        }
        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.deletePostComment(memberId,postId,commentId);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }

        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @GetMapping("/post/{member_id}/{post_id}/comment")
    @ApiOperation(value = "게시글 답변 조회")
    public ResponseEntity<?> viewPostComments(
            @PathVariable("member_id") Long memberId,
            @PathVariable("post_id") Long postId,
            @ApiParam(value = "토큰") @RequestHeader("Authorization") String token) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        JSONArray jsonArray = postService.viewComments(postId, memberIdByToken.getAccountName());
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }

    @PostMapping("/post/{member_id}/{post_id}/comment/{comment_id}/recommend")
    @ApiOperation(value = "추천 답변 등록")
    public ResponseEntity registerRecommendComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {

        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));

        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.registerRecommendComment(memberIdByToken.getId(), postId, commentId);
        } catch(Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }

        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @PostMapping("/post/{user_id}/{post_id}/comment/{comment_id}/ban")
    @ApiOperation(value = "댓글 신고하기")
    public ResponseEntity banComment(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        Long reportMember = memberIdByToken.getId();
        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.banComment(memberId,postId,commentId,reportMember);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }

        param.put("result", true);
        return ResponseEntity.ok(param);
    }
}
