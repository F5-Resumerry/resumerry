package com.f5.resumerry.Post.controller;

import com.f5.resumerry.Post.dto.FindPostDTO;
import com.f5.resumerry.Post.dto.UpdatePostDTO;
import com.f5.resumerry.Post.dto.RegisterPostDTO;
import com.f5.resumerry.Post.service.PostService;
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

    @GetMapping(value = "/post")
    public ResponseEntity findPosts(@RequestParam(name = "title", required = false, defaultValue = "temp") String title,
                                    @RequestParam(name = "category",required = false, defaultValue = "DEVELOPMENT") String category,
                                    @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort
                                    ) {
        List<FindPostDTO> findPostResponse = postService.findPosts(title, category, sort);
        return  ResponseEntity.ok(findPostResponse);
    }

    @GetMapping(value = "/post/{user_id}")
    public ResponseEntity findPostsInMypage(@PathVariable("user_id") Long id) {
        List<FindPostDTO> findPostsInMypageResponse =postService.findPostsInMypage(id);
        return ResponseEntity.ok(findPostsInMypageResponse);
    }

    @GetMapping(value = "/post/{member_id}/{post_id}")
    public ResponseEntity viewPost(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId) {
        FindPostDTO viewPostResponse = postService.viewPost(memberId, postId);
        return ResponseEntity.ok(viewPostResponse);
    }

    @PostMapping(value = "/post/{user_id}")
    public ResponseEntity registerPost(@PathVariable("user_id") Long id, @RequestBody RegisterPostDTO registerPostDTO) {
        postService.registerPosts(id, registerPostDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/post/{member_id}/{post_id}")
    public ResponseEntity putPost(@PathVariable("member_id") Long memberId, @PathVariable("post_id") Long postId, @RequestBody UpdatePostDTO putPostDTO) {
        postService.updatePost(memberId, postId, putPostDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/post/test")
    public String TestPosts(@RequestParam(name = "category",required = false, defaultValue = "DEVELOPMENT") String category,
                            @RequestParam(name = "title", required = false, defaultValue = "") String title,
                            @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort){
        return "postTest";
    }
}
