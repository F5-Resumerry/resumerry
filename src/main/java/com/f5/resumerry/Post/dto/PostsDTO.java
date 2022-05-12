package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostsDTO {
    private Long postId;
    private String title;
    private String contents;
    private Integer commentCnt;
    private Integer views;
    private Boolean isAnonymous;
    private String imageSrc;
    private Long memberId;
    private String nickname;
    private LocalDateTime modifiedDate;
    private CategoryEnum category;
}
