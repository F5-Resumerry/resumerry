package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.*;


import java.util.List;

public interface PostCustomRepository {

    //List<PostDTO> viewPosts(PostDTO p);
    List<FindPostDTO> findPosts(String title, String category, String sort);
    List<FindPostDTO> findPostsInMyPage(Long id);
    FindPostDTO viewPost(Long memberId, Long postId);
    void registerPost(RegisterPostDTO r);

    // 댓글
    void registerPostComment(PostCommentDTO pc);

    //List<PostCommentDepthDTO> findCommentDepth(Integer Index, Long postId);

    //PostCommentDTO findComment(Integer index, Long postId);

    void registerRecommendComment(PostCommentRecommendDTO pcr);





}
