package com.f5.resumerry.Resume.dto;


import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeCommentRecommend;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RegisterResumeDTO {

    private Long id;

    private String title;

    private String contents;

    private Integer years;

    private String fileLink;

    private Integer viewCnt;

    private CategoryEnum category;

    private Boolean isDelete;

    private Long memberId;


    @Builder
    public RegisterResumeDTO(Long id, String title, String contents, Integer years, String fileLink, Integer viewCnt, CategoryEnum category, Boolean isDelete, Long memberId){
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.years = years;
        this.fileLink = fileLink;
        this.viewCnt = viewCnt;
        this.category = category;
        this.isDelete = isDelete;
        this.memberId = memberId;
    }

    public Resume toEntity(){
        Resume build = Resume.builder()
                .id(id)
                .title(title)
                .contents(contents)
                .years(years)
                .fileLink(fileLink)
                .viewCnt(viewCnt)
                .category(category)
                .isDelete(isDelete)
                .memberId(memberId)
                .build();
        return build;
    }
}