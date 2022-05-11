package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.exception.DuplicateException;
import com.f5.resumerry.exception.ErrorCode;
import com.f5.resumerry.security.AuthController;
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
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    private AuthController authController;

//    @GetMapping(value = "/posts")
//    public ResponseEntity viewPosts() {
//        List<PostDTO> viewPostsResponse = postService.viewPosts();
//        return ResponseEntity.ok(viewPostsResponse);
//    }


    @GetMapping(value = "/posts")
    @ApiOperation(value = "게시글 목록 조회")
    public ResponseEntity findPosts(@ApiParam(value = "글 제목") @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                    @ApiParam(value = "직종") @RequestParam(name = "category",required = false, defaultValue = "ALL") String category,
                                    @ApiParam(value = "정렬기준") @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort
                                    ) {
        List<FindPostDTO> findPostResponse = postService.findPosts(title, category, sort);
        return  ResponseEntity.ok(findPostResponse);
    }

    @GetMapping(value = "/post/{user_id}")
    @ApiOperation(value = "내 페이지에서 게시글 조회")
    public ResponseEntity findPostsInMyPage(@ApiParam(value = "회원 번호") @PathVariable("user_id") Long id) {
        List<FindPostDTO> findPostsInMypageResponse =postService.findPostsInMyPage(id);
        return ResponseEntity.ok(findPostsInMypageResponse);
    }

    @GetMapping(value = "/post/{member_id}/{post_id}")
    @ApiOperation(value = "게시글 상세 조회")
    public ResponseEntity viewPost(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId
    ) {
        FindPostDTO viewPostResponse = postService.viewPost(memberId, postId);
        return ResponseEntity.ok(viewPostResponse);
    }

    @PostMapping(value = "/post")
    @ApiOperation(value = "게시글 등록")
    public ResponseEntity registerPost(
            @ApiParam(value = "게시글 DTO") @RequestBody RegisterPostDTO registerPostDTO,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        String account_name = authController.Token2Username(token); // token 을 통해 받은 account_name
        Member memberIdByToken = memberService.getMember(account_name);
        postService.registerPosts(memberIdByToken.getId(), registerPostDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/post/{member_id}/{post_id}") // 게시글 수정
    @ApiOperation(value = "게시글 수정")
    public ResponseEntity putPost(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "게시글 수정 DTO") @RequestBody UpdatePostDTO putPostDTO,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        String account_name = authController.Token2Username(token); // token 을 통해 받은 account_name
        Member memberIdByToken = memberService.getMember(account_name);
        if(memberId != memberIdByToken.getId()) {
                //게시글 수정 권한이 없습니다.
            throw new DuplicateException("modify", "게시글 수정 권한이 없습니다", ErrorCode.INVALID_INPUT_VALUE);
        }
        postService.updatePost(memberId, postId, putPostDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{member_id}/{post_id}")
    @ApiOperation(value = "게시글 삭제")
    public ResponseEntity DeletePost(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId
    ) {
        postService.deletePost(memberId,postId);
        return ResponseEntity.ok().build();
    }

    // comment controller
    @PostMapping("/post/{member_id}/{post_id}/comment")
    @ApiOperation(value = "게시글 답변 등록")
    public ResponseEntity registerPostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "게시글 답변 DTO") @RequestBody PostCommentDTO postCommentDTO
    ) {
        postService.registerPostComment(memberId, postId, postCommentDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/post/{member_id}/{post_id}/comment/{comment_id}")
    @ApiOperation(value = "게시글 답변 삭제")
    public ResponseEntity deletePostComment(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId
    ) {
        postService.deletePostComment(memberId,postId,commentId);
        return ResponseEntity.ok().build();
    }

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
            @ApiParam(value = "답변 번호") @PathVariable("comment_id") Long commentId
    ) {
        postService.registerRecommendComment(memberId, postId, commentId);
        return ResponseEntity.ok().build();
    }


}
