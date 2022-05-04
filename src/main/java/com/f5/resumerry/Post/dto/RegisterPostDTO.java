package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

@Data
@NoArgsConstructor
public class RegisterPostDTO {

    private String title;
    private CategoryEnum category;
    private String contents;
    private String fileLink;
    private boolean isAnonymous;


    @Builder
    public RegisterPostDTO(String title, CategoryEnum category, String contents, String fileLink, boolean isAnonymous) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.fileLink = fileLink;
        this.isAnonymous = isAnonymous;
    }

}
