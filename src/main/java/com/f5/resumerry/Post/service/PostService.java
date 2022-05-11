package com.f5.resumerry.Post.service;

import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public List<FindPostDTO> findPosts(String title, String category, String sort) {

        List<FindPostDTO> posts = postRepository.findPosts(title, category, sort);
        return posts;

    }

    public void registerPosts(Long memberId, RegisterPostDTO req){
        RegisterPostDTO insertPost = new RegisterPostDTO(req.getTitle(), req.getCategory(), req.getContents(), req.getFileLink(), req.getIsAnonymous(),0, memberId, req.getResumeId());
        postRepository.registerPost(insertPost);
    }

    public FindPostDTO viewPost(Long memberId, Long postId) {
        postRepository.updateViewCnt(memberId, postId);
        return postRepository.viewPost(memberId, postId);
     }

    public List<FindPostDTO> findPostsInMyPage(Long memberId) {
        return postRepository.findPostsInMyPage(memberId);
    }

    public void updatePost(Long memberId, Long postId, UpdatePostDTO req) {
        postRepository.updatePost(memberId,postId,req.getCategory(),req.getContents(),req.getIsAnonymous(),req.getTitle());
    }

    public void deletePost (Long memberId, Long postId) {
        //is_delete 컬럼 기본값 n update 형식으로 y로 수정
        postRepository.updateIsDelete(memberId, postId);
    }

    // 댓글 controller 시작
    public void registerPostComment(Long memberId, Long postId, GetCommentDTO req) {
        PostCommentDTO postCommentDTO = new PostCommentDTO(req.getContents(), req.getPostCommentGroup(), req.getPostCommentDepth(), req.getIsAnonymous(), memberId, postId);
        postRepository.registerPostComment(postCommentDTO);
    }

    public void deletePostComment(Long memberId, Long postId, Long commentId) {
        postRepository.updateCommentIsDelete(memberId, postId,commentId);
    }

    public void registerRecommendComment(Long memberId, Long postId, Long commentId) {
        // postd, commentid를 가진 댓글에 PostCommentRecommend 테이블에 memberid와 commendId 추가
        PostCommentRecommendDTO pcr = new PostCommentRecommendDTO(memberId, postId, commentId);
        postRepository.registerRecommendComment(pcr);

    }

    public void banComment(Long memberId, Long postId, Long commentId, Long reportMember) {
        postRepository.banComment(postId,commentId,reportMember);
    }

//    public List<PostCommentDTO> viewComments(Long memberId, Long postId) {
//        // 대댓글 리스트
//        //ObjectMapper mapper = new ObjectMapper();
//        List<PostCommentDTO> comments = new ArrayList<PostCommentDTO>();
//    // 반복문 수정하기
//        for(int i = 0; i < postRepository. ; i++) {
//            // i 번째 그룹의 대댓글들을 가져옴
//           List<PostCommentDepthDTO> commentDepthList = postRepository.findCommentDepth(i, postId);
//           // 그룹 i번의 댓글을 가져옴
//           PostCommentDTO comment = postRepository.findComment(i,postId);
//           // 그룹 i번쨰 댓글에 depthlist 주입
//           comment.setPostCommentDepthList(commentDepthList);
//           comments.add(comment);
//            System.out.println(comment);
//        }
//        return comments;
//
//    }


}
