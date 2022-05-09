package com.f5.resumerry.Post.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.domain.entity.MemberInfo;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PostDTO {
    // 여기에 dto contructure 정리 예정
    private Long id;
    private String title;
    private Integer views;
    private CategoryEnum category;
    private Boolean isAnonymous;
    private Long memberId;
    private Long resumeId;







}
