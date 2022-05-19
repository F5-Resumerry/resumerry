package com.f5.resumerry.Resume.dto;

import lombok.Data;

@Data
public class GetCommentDTO {

    private String contents;
    private Integer commentGroup;
    private Integer commentDepth;
    private Boolean isAnonymous;

}
