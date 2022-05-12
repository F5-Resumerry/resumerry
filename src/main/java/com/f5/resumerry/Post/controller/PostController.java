package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.service.JwtUtil;
import com.f5.resumerry.Member.service.MemberService;
import com.f5.resumerry.Post.dto.FindPostDTO;
import com.f5.resumerry.Post.dto.PostsDTO;
import com.f5.resumerry.Post.dto.RegisterPostDTO;
import com.f5.resumerry.Post.dto.UpdatePostDTO;
import com.f5.resumerry.Post.service.PostService;
import com.f5.resumerry.exception.AuthenticateException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/posts")
    @ApiOperation(value = "게시글 목록 조회")
    public ResponseEntity findPosts(@ApiParam(value = "글 제목") @RequestParam(name = "title", required = false, defaultValue = "") String title,
                                    @ApiParam(value = "직종") @RequestParam(name = "category",required = false, defaultValue = "ALL") String category,
                                    @ApiParam(value = "정렬기준") @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort
                                    ) {
        List<PostsDTO> findPostResponse = postService.findPosts(title, category, sort);
        return  ResponseEntity.ok(findPostResponse);
    }

    @GetMapping(value = "/post/{user_id}")
    @ApiOperation(value = "내 페이지에서 게시글 조회")
    public ResponseEntity findPostsInMyPage(@ApiParam(value = "회원 번호") @PathVariable("user_id") Long user_id,
                                            @ApiParam(value = "인증 토큰") @RequestHeader String token) {

        Member memberByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if(!user_id.equals(memberByToken.getId())) {
            throw new AuthenticateException("잘못된 회원입니다.");
        }
        List<PostsDTO> findPostsInMyPageResponse =postService.findPostsInMyPage(memberByToken.getId());
        return ResponseEntity.ok(findPostsInMyPageResponse);
    }

    @GetMapping(value = "/post/{user_id}/{post_id}")
    @ApiOperation(value = "게시글 상세 조회")
    public ResponseEntity viewPost(
            @ApiParam(value = "회원 번호") @PathVariable("user_id") Long userId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "유저 토큰") @RequestHeader String token
    ) {
        Member memberByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        FindPostDTO viewPostResponse = postService.viewPost(userId, postId, memberByToken.getId());
        return ResponseEntity.ok(viewPostResponse);
    }

    @PostMapping(value = "/post")
    @ApiOperation(value = "게시글 등록")
    public ResponseEntity<Map<String, Boolean>> registerPost(
            @ApiParam(value = "게시글 DTO") @RequestBody RegisterPostDTO registerPostDTO,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    )  {
        Member memberByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.registerPosts(memberByToken.getId(), registerPostDTO);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @PutMapping(value = "/post/{member_id}/{post_id}") // 게시글 수정
    @ApiOperation(value = "게시글 수정")
    public ResponseEntity putPost(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "게시글 수정 DTO") @RequestBody UpdatePostDTO putPostDTO,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));
        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }
        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.updatePost(memberId, postId, putPostDTO);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok(param);
    }

    @DeleteMapping("/post/{member_id}/{post_id}")
    @ApiOperation(value = "게시글 삭제")
    public ResponseEntity DeletePost(
            @ApiParam(value = "회원 번호") @PathVariable("member_id") Long memberId,
            @ApiParam(value = "게시글 번호") @PathVariable("post_id") Long postId,
            @ApiParam(value = "인증 토큰") @RequestHeader("Authorization") String token
    ) {
        Member memberIdByToken = memberService.getMember(jwtUtil.extractUsername(token.substring(7)));

        if (!memberId.equals(memberIdByToken.getId())) {
            throw new AuthenticateException("회원의 아이디가 같지 않습니다.");
        }

        Map<String, Boolean> param = new HashMap<>();
        try {
            postService.deletePost(memberIdByToken.getId(),postId);
        } catch (Exception e) {
            param.put("result", false);
            return ResponseEntity.ok(param);
        }
        param.put("result", true);
        return ResponseEntity.ok(param);
    }

}
