package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.GetCommentDTO;
import com.f5.resumerry.Post.dto.PostCommentDTO;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
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
        String jwt = token.substring(7);
        String account_name = jwtUtil.extractUsername(jwt);
        Member memberIdByToken = memberService.getMember(account_name);
        postService.registerPostComment(memberIdByToken.getId(), postId, getCommentDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/post/{member_id}/{post_id}/comment/{comment_id}")
    @ApiOperation(value = "게시글 답변 삭제")
    public ResponseEntity deletePostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        String account_name = jwtUtil.extractUsername(jwt);
        Member memberIdByToken = memberService.getMember(account_name);
        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("삭제하기 위한 회원의 아이디가 같지 않습니다.");
        }
        postService.deletePostComment(memberId,postId,commentId);
        return ResponseEntity.ok().build();
    }
//todo.  댓글 리스트 확인
//    @GetMapping("/post/{member_id}/{post_id}/comment")
//    public ResponseEntity viewPostComments(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @RequestBody String req) {
//        List<PostCommentDTO> viewPostComments = postService.viewComments(memberId, postId);
//        return ResponseEntity.ok(viewPostComments);
//    }

    @PostMapping("/post/{member_id}/{post_id}/comment/{comment_id}/recommend")
    @ApiOperation(value = "추천 답변 등록")
    public ResponseEntity registerRecommendComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        String account_name = jwtUtil.extractUsername(jwt);
        Member memberIdByToken = memberService.getMember(account_name);
        postService.registerRecommendComment(memberIdByToken.getId(), postId, commentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/{user_id}/{post_id}/comment/{comment_id}/ban")
    @ApiOperation(value = "댓글 신고하기")
    public ResponseEntity banComment(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7);
        String account_name = jwtUtil.extractUsername(jwt);
        Member memberIdByToken = memberService.getMember(account_name);
        Long reportMember = memberIdByToken.getId();
        postService.banComment(memberId,postId,commentId,reportMember);
        return ResponseEntity.ok().build();
    }
}
