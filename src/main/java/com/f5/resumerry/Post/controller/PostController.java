package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.exception.DuplicateException;
import com.f5.resumerry.exception.ErrorCode;
import com.f5.resumerry.security.AuthController;
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
    public ResponseEntity findPosts(@RequestParam(name = "title", required = false, defaultValue = "") String title,
                                    @RequestParam(name = "category",required = false, defaultValue = "ALL") String category,
                                    @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort
                                    ) {
        List<FindPostDTO> findPostResponse = postService.findPosts(title, category, sort);
        return  ResponseEntity.ok(findPostResponse);
    }

    @GetMapping(value = "/post/{user_id}")
    public ResponseEntity findPostsInMyPage(@PathVariable("user_id") Long id) {
        List<FindPostDTO> findPostsInMypageResponse =postService.findPostsInMyPage(id);
        return ResponseEntity.ok(findPostsInMypageResponse);
    }

    @GetMapping(value = "/post/{member_id}/{post_id}")
    public ResponseEntity viewPost(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId) {
        FindPostDTO viewPostResponse = postService.viewPost(memberId, postId);
        return ResponseEntity.ok(viewPostResponse);
    }

    @PostMapping(value = "/post")
    public ResponseEntity registerPost(@RequestBody RegisterPostDTO registerPostDTO, @RequestHeader("Authorization") String token) {
        String account_name = authController.Token2Username(token); // token 을 통해 받은 account_name
        Member memberIdByToken = memberService.getMember(account_name);
        postService.registerPosts(memberIdByToken.getId(), registerPostDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/post/{member_id}/{post_id}") // 게시글 수정
    public ResponseEntity putPost(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @RequestBody UpdatePostDTO putPostDTO, @RequestHeader("Authorization") String token) {
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
    public ResponseEntity DeletePost(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId) {
        postService.deletePost(memberId,postId);
        return ResponseEntity.ok().build();
    }

    // comment controller
    @PostMapping("/post/{member_id}/{post_id}/comment")
    public ResponseEntity registerPostComment(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @RequestBody PostCommentDTO postCommentDTO) {
        postService.registerPostComment(memberId, postId, postCommentDTO);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/post/{member_id}/{post_id}/comment/{comment_id}")
    public ResponseEntity deletePostComment(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @PathVariable("comment_id") Long commentId) {
        postService.deletePostComment(memberId,postId,commentId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/post/{member_id}/{post_id}/comment")
//    public ResponseEntity viewPostComments(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @RequestBody String req) {
//        List<PostCommentDTO> viewPostComments = postService.viewComments(memberId, postId);
//        return ResponseEntity.ok(viewPostComments);
//    }

    @PostMapping("/post/{member_id}/{post_id}/comment/{comment_id}/recommend")
    public ResponseEntity registerRecommendComment(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @PathVariable("comment_id") Long commentId) {
        postService.registerRecommendComment(memberId, postId, commentId);
        return ResponseEntity.ok().build();
    }


}
