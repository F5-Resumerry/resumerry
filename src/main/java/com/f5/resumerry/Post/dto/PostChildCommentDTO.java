package com.f5.resumerry.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostChildCommentDTO {

   private Long commentId;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private String contents;
    private Integer recommendCnt;
    private Integer banCnt;
    private Boolean isAnonymous;
    private Boolean isOwner;
    private LocalDateTime modifiedDate;
    private Integer postCommentDepth;
    private Integer PostCommentGroup;

}
