package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPostDTO {

    private Long postId;
    private String title;
    private String contents;
    private Integer commentCnt;
    private Integer viewCnt;
    private Boolean isAnonymous;
    private String imageSrc;
    private Long memberId;
    private String nickname;
    private LocalDateTime modifiedDate;
    private CategoryEnum category;
    private Boolean isOwner;
}
