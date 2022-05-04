package com.f5.resumerry.Post.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.domain.entity.MemberInfo;
import com.f5.resumerry.Post.entity.Post;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {

    private Long id;
    private String title;
    private Integer views;
    private CategoryEnum category;
    private Boolean isAnonymous;
    private Member member;
    private Resume resume;

    @Builder
    public PostDTO(Long id) {
        this.id = id;
        this.title = "";
        this.views = 0;
        this.category = getCategory();
        this.isAnonymous = Boolean.TRUE;
    }

    public Post toEntity(){
        Post build = Post.builder()
                .id(id)
                .title(title)
                .views(views)
                .category(category)
                .isAnonymous(isAnonymous)
                .build();
        return build;
    }
}
