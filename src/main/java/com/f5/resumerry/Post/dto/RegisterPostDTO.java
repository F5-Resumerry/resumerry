package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterPostDTO {
    private String title;
    private CategoryEnum category;
    private String contents;
    private String fileLink;
    private Boolean isAnonymous;
    private Integer viewCnt;
    private Long memberId;
    private Long resumeId;

}
