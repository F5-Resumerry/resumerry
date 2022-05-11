package com.f5.resumerry.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentRecommendDTO {
    private Long memberId;
    private Long postId;
    private Long commentId;
}
