package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePostDTO {
    private CategoryEnum category;
    private String title;
    private String contents;
    private Boolean isAnonymous;

    public UpdatePostDTO(CategoryEnum category, String title, String contents, Boolean isAnonymous) {
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.isAnonymous = isAnonymous;
    }
}
