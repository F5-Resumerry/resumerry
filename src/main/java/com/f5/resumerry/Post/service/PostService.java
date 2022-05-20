package com.f5.resumerry.Post.service;

import com.f5.resumerry.Member.repository.MemberInfoRepository;
import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
import com.f5.resumerry.Post.repository.PostCommentRecommendRepository;
import com.f5.resumerry.Post.repository.PostCommentReportRepository;
import com.f5.resumerry.Post.repository.PostCommentRepository;
import com.f5.resumerry.Post.repository.PostRepository;
import com.f5.resumerry.Resume.Resume;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentRecommendRepository postCommentRecommendRepository;

    @Autowired
    private PostCommentReportRepository postCommentReportRepository;

    @Autowired
    private MemberInfoRepository memberInfoRepository;


    public List<PostsDTO> findPosts(String title, String category, String sort) {
        /**
         * ToDo
         * recent / view
         * 1. 조건이 최신순(recent)이다.
         *  1.1. 카테고리가 전체이거나 혹은 특정 카테고리거나.
         * 2. 조건이 (view)이다.
         *  2.1. 카테고리가 전체이거나 혹은 특정 카테고리거나.
         */
        if (sort.equals("recent")) {
            if(category.equals("ALL")) {
                return postRepository.findPosts(title, category);
            }
            return postRepository.findPostsNotAll(title, category);
        } else {
            if (category.equals("ALL")) {
                return postRepository.findPostsView(title, category);
            }
            return postRepository.findPostsViewNotAll(title, category);
        }

    }

    public void registerPosts(Long memberId, RegisterPostDTO req){
        RegisterPostDTO insertPost = new RegisterPostDTO(req.getTitle(), req.getCategory(), req.getContents(), req.getFileLink(), req.getIsAnonymous(),0, memberId);
        postRepository.registerPost(insertPost);
        memberInfoRepository.updateReward(memberId, 3, 0);
    }

    public FindPostDTO viewPost(Long memberId, Long postId, Long tokenId) {
        postRepository.viewCnt(memberId, postId);
        if (memberId != tokenId) {
            // 소유자가 아닌경우
            postRepository.updateViewCnt(memberId, postId);
            return postRepository.viewNotOwnPost(postId);
        } else {
            return postRepository.viewPost(tokenId,postId);
        }
     }

    public List<PostsDTO> findPostsInMyPage(Long memberId) {
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
        PostCommentDTO postCommentDTO = new PostCommentDTO(req.getContents(), req.getCommentGroup(), req.getCommentDepth(), req.getIsAnonymous(), memberId, postId);
        postRepository.registerPostComment(postCommentDTO);
        memberInfoRepository.updateReward(memberId, 5, 0);
    }

    public void deletePostComment(Long memberId, Long postId, Long commentId) {
        postRepository.updateCommentIsDelete(memberId, commentId);
    }

    public void registerRecommendComment(Long memberId, Long postId, Long commentId) {
        // postd, commentid를 가진 댓글에 PostCommentRecommend 테이블에 memberid와 commendId 추가
        PostCommentRecommendDTO pcr = new PostCommentRecommendDTO(memberId, postId, commentId);
        postRepository.registerRecommendComment(pcr);
        Optional<PostComment> postCommentOptional = postCommentRepository.findById(commentId);
        PostComment postComment = postCommentOptional.orElse(null);
        if(postComment.getPostCommentRecommendList().size() % 5 == 0){
            memberInfoRepository.updateReward(postComment.getMemberId(), 5, 1);
        }

    }

    public void banComment(Long memberId, Long postId, Long commentId, Long reportMember) {
        postRepository.banComment(postId,commentId,reportMember);
    }

    public JSONArray viewComments(Long postId, Long memberId) {

        ArrayList<Long>[] arrayList = new ArrayList[100];
        for(int i = 0; i <  100; i++){
            arrayList[i] = new ArrayList<Long>();
        }
        List<PostComment> postComments = postCommentRepository.findByPostId(postId);
        for(PostComment postComment: postComments){
            arrayList[postComment.getPostCommentGroup()].add(postComment.getId());
        }
        JSONArray jsonArray = new JSONArray();
        for(ArrayList arrayList1: arrayList){
            int count = 0;
            JSONObject group = new JSONObject();
            JSONArray depth = new JSONArray();

            for(Object id: arrayList1){
                Optional<PostComment> postCommentOptional = postCommentRepository.findById((Long) id);
                PostComment postComment = postCommentOptional.orElse(null);
                JSONObject depthIn = new JSONObject();
                if(count == 0){
                    group.put("commentId", id);
                    group.put("memberId", postComment.getMember().getId());
                    group.put("imageSrc", postComment.getMember().getImageSrc());
                    group.put("nickname", postComment.getMember().getNickname());
                    group.put("contents", postComment.getContents());
                    group.put("recommendCnt", postComment.getPostCommentRecommendList().size());
                    group.put("banCnt", postComment.getPostCommentReportList().size());
                    group.put("isAnonymous", postComment.getIsAnonymous());
                    group.put("modifiedDate", postComment.getModifiedDate().toString());
                    group.put("commentGroup", postComment.getPostCommentGroup());
                    group.put("commentDepth", postComment.getPostCommentDepth());
                    group.put("isOwner", postComment.getMember().getId() == memberId ? true : false);
                    group.put("isDelete", postComment.getIsDelete());
                    group.put("isRecommend", postCommentRecommendRepository.existsByMemberIdAndPostCommentId(memberId, postComment.getId()));
                    group.put("isBanned", postCommentReportRepository.existsByMemberIdAndPostCommentId(memberId, postComment.getId()));
                    count += 1;
                    continue;
                }
                depthIn.put("commentId", id);
                depthIn.put("memberId", postComment.getMember().getId());
                depthIn.put("imageSrc", postComment.getMember().getImageSrc());
                depthIn.put("nickname", postComment.getMember().getNickname());
                depthIn.put("contents", postComment.getContents());
                depthIn.put("recommendCnt", postComment.getPostCommentRecommendList().size());
                depthIn.put("banCnt", postComment.getPostCommentReportList().size());
                depthIn.put("isAnonymous", postComment.getIsAnonymous());
                depthIn.put("modifiedDate", postComment.getModifiedDate().toString());
                depthIn.put("commentGroup", postComment.getPostCommentGroup());
                depthIn.put("commentDepth", postComment.getPostCommentDepth());
                depthIn.put("isOwner", postComment.getMember().getId() == memberId ? true : false);
                depthIn.put("isDelete", postComment.getIsDelete());
                depthIn.put("isRecommend", postCommentRecommendRepository.existsByMemberIdAndPostCommentId(memberId, postComment.getId()));
                depthIn.put("isBanned", postCommentReportRepository.existsByMemberIdAndPostCommentId(memberId, postComment.getId()));
                depth.add(depthIn);
            }
            log.info(String.valueOf(depth));
            if(group.size() > 0) {
                group.put("childComments", depth);
                jsonArray.add(group);
            }
        }
        log.info(String.valueOf(jsonArray));
        return jsonArray;
    }


}
