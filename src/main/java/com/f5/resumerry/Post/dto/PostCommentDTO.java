package com.f5.resumerry.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDTO {
//   is_anonymous  post_comment_depth post_comment_group  member_id  post_id  is_delete);
    private Long memberId;
    private Long postId;

    private String imageSrc;
    private String nickname;
    private String contents;
    private Integer recommendCnt;
    private Integer BanCnt;
    private Boolean isAnonymous;
    private Integer postCommentGroup;
    private Integer postCommentDepth;

    private List<PostChildCommentDTO> postCommentDepthList;

    public PostCommentDTO(String contents, Integer postCommentGroup, Integer postCommentDepth, Boolean isAnonymous, Long memberId, Long postId) {
        this.contents = contents;
        this.postCommentGroup = postCommentGroup;
        this.postCommentDepth = postCommentDepth;
        this.isAnonymous = isAnonymous;
        this.memberId = memberId;
        this.postId = postId;
    }



}
