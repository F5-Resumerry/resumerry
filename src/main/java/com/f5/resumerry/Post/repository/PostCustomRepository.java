package com.f5.resumerry.Post.repository;

import com.f5.resumerry.Post.dto.*;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PostCustomRepository {

    //List<PostDTO> viewPosts(PostDTO p);
    List<PostsDTO> findPosts(String title, String category);
    List<PostsDTO> findPostsInMyPage(Long id);
    FindPostDTO viewPost(Long memberId, Long postId);
    void registerPost(RegisterPostDTO r);

    // 댓글
    void registerPostComment(PostCommentDTO pc);

    //List<PostCommentDepthDTO> findCommentDepth(Integer Index, Long postId);

    //PostCommentDTO findComment(Integer index, Long postId);

    void registerRecommendComment(PostCommentRecommendDTO pcr);

    void banComment(Long postId, Long commentId, Long reportMember);


    List<PostsDTO> findPostsView(String title, String category);

    List<PostsDTO> findPostsNotAll(String title, String category);

    List<PostsDTO> findPostsViewNotAll(String title, String category);

    FindPostDTO viewNotOwnPost(Long postId);

    //List<PostParentCommentDTO> findComments(Long postId, List<PostChildCommentDTO> pcc);

    List<PostChildCommentDTO> findChildComments(Integer groupNum, Long postId);

    PostChildCommentDTO findParentComment(Integer groupNum, Long postId);

    List<PostChildCommentDTO> stCommentsByGroup(Long postId);
}
