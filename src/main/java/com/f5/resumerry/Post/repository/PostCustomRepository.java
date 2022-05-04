package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.FindPostDTO;

import java.util.List;

public interface PostCustomRepository {
    List<FindPostDTO> findPosts(String title, String category, String sort);
    List<FindPostDTO> findPostsInMypage(Long id);
    FindPostDTO viewPost(Long memberId, Long postId);


}
