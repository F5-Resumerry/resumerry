package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ViewPostDTO {

    private CategoryEnum category;
    private String title;
    private String contents;
    private String fileLink;
    private LocalDate modifiedDate;
    private Boolean isAnonymous;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private Integer viewCnt;
    private Integer commentCnt;

}
