package com.f5.resumerry.Post.service;

import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.entity.PostComment;
import com.f5.resumerry.Post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
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
        RegisterPostDTO insertPost = new RegisterPostDTO(req.getTitle(), req.getCategory(), req.getContents(), req.getFileLink(), req.getIsAnonymous(),0, memberId, req.getResumeId());
        postRepository.registerPost(insertPost);
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
        //is_delete 컬럼 기본값 n update 형식으로 y로 수정
        postRepository.updateIsDelete(memberId, postId);
    }

    // 댓글 controller 시작
    public void registerPostComment(Long memberId, Long postId, PostCommentDTO req) {
        PostCommentDTO postCommentDTO = new PostCommentDTO(req.getContents(), req.getPostCommentGroup(), req.getPostCommentDepth(), req.getIsAnonymous(), memberId, postId);
        postRepository.registerPostComment(postCommentDTO);
    }

    public void deletePostComment(Long memberId, Long postId, Long commentId) {
        postRepository.updateCommentIsDelete(memberId, postId,commentId);
    }

    public List<PostCommentDTO> viewComments(Long memberId, Long postId) {
        // 대댓글 리스트
        //ObjectMapper mapper = new ObjectMapper();
        List<PostCommentDTO> comments = new ArrayList<PostCommentDTO>();
    // 반복문 수정하기
        for(int i = 0; i < 5 ; i++) {
            // i 번째 그룹의 대댓글들을 가져옴
           List<PostCommentDepthDTO> commentDepthList = postRepository.findCommentDepth(i, postId);
           // 그룹 i번의 댓글을 가져옴
           PostCommentDTO comment = postRepository.findComment(i,postId);
           // 그룹 i번쨰 댓글에 depthlist 주입
           comment.setPostCommentDepthList(commentDepthList);
           comments.add(comment);
            System.out.println(comment);
        }

        return comments;

    }


}
