package com.f5.resumerry.Post.service;

import com.f5.resumerry.Post.dto.*;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Post.entity.PostComment;
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
    }

    public FindPostDTO viewPost(Long memberId, Long postId, Long tokenId) {
        postRepository.viewCnt(memberId, postId);
        if (memberId != tokenId) {
            // 소유자가 아닌경우
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

    public JSONArray viewComments(Long postId, String accountName) {

        ArrayList<Long>[] arrayList = new ArrayList[100];
        for(int i = 0; i <  100; i++){
            arrayList[i] = new ArrayList<Long>();
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.orElse(null);
        List<PostComment> postComments = postCommentRepository.findByPost(post);
        for(PostComment postComment: postComments){
            arrayList[postComment.getPostCommentGroup()].add(postComment.getId());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(ArrayList arrayList1: arrayList){
//            if(!arrayList1.isEmpty()){
//                break;
//            }
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
                    group.put("postCommentGroup", postComment.getPostCommentGroup());
                    group.put("postCommentDepth", postComment.getPostCommentDepth());
                    group.put("isOwner", postComment.getMember().getAccountName() == accountName ? true : false);
                    group.put("isDelete", postComment.getIsDelete());
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
                depthIn.put("postCommentGroup", postComment.getPostCommentGroup());
                depthIn.put("postCommentDepth", postComment.getPostCommentDepth());
                depthIn.put("isOwner", postComment.getMember().getAccountName() == accountName ? true : false);
                depthIn.put("isDelete", postComment.getIsDelete());
                depth.add(depthIn);
            }
            log.info(String.valueOf(depth));
            if(group.size() > 0) {
                group.put("postChildComments", depth);
                jsonArray.add(group);
            }
        }
        log.info(String.valueOf(jsonArray));
        return jsonArray;
    }


}
