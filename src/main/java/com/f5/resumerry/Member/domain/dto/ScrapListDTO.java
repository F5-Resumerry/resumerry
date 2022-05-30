package com.f5.resumerry.Member.domain.dto;


import com.f5.resumerry.Resume.Hashtag;
import com.f5.resumerry.selector.CategoryEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScrapListDTO {

    private Long memberId;
    private Long resumeId;
    private String imageSrc;
    private String nickname;
    private Integer recommendCnt;
    private Integer commentCnt;
    private String title;
    private CategoryEnum category;
    private String contents;
    private String fileLink;
    private Integer viewCnt;
    private Integer years;
    private LocalDateTime modifiedDate;
    private List<String> hashtagList;

}
