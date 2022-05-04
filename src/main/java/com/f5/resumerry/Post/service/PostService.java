package com.f5.resumerry.Post.service;

import com.f5.resumerry.Post.dto.FindPostDTO;
import com.f5.resumerry.Post.dto.UpdatePostDTO;
import com.f5.resumerry.Post.dto.RegisterPostDTO;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<FindPostDTO> findPosts(String title, String category, String sort) {
        return postRepository.findPosts(title, category, sort);
    }

    public void registerPosts(Long memberId, RegisterPostDTO req){
        Post registerPost = new Post(req.getTitle(), req.getContents(), 0, req.getCategory(), req.isAnonymous(), memberId);
        postRepository.save(registerPost);
    }

    public FindPostDTO viewPost(Long memberId, Long postId) {
        postRepository.updateViewCnt(memberId, postId);
        return postRepository.viewPost(memberId, postId);
     }

    public List<FindPostDTO> findPostsInMypage(Long memberId) {
        return postRepository.findPostsInMypage(memberId);
    }

    public void updatePost(Long memberId, Long postId, UpdatePostDTO req) {
        postRepository.updatePost(memberId,postId,req.getCategory(),req.getContents(),req.getIsAnonymous(),req.getTitle());
    }

    public void deletePost (Long memberId, Long postId) {
        postRepository.deleteAllByMemberIdAndId(memberId, postId);
    }
}
