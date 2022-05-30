package com.f5.resumerry.Post.dto;

import lombok.Data;

@Data
public class GetCommentDTO {

    private String contents;
    private Integer commentGroup;
    private Integer commentDepth;
    private Boolean isAnonymous;

}
