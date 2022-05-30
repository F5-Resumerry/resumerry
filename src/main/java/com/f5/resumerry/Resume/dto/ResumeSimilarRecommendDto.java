package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.ResumeSimilarRecommend;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeSimilarRecommendDto {

    private Long id;
    private Integer viewCnt;
    private Integer commentCnt;
    private Integer likeCnt;
    private String title;
    private String fileLink;
    private String contents;
    private LocalDateTime createdDate;

    @Builder
    public ResumeSimilarRecommendDto(Long id, Integer viewCnt, String title, String fileLink, String contents, LocalDateTime createdDate) {
        this.id = id;
        this.viewCnt = viewCnt;
        this.title = title;
        this.fileLink = fileLink;
        this.contents = contents;
        this.createdDate = createdDate;
    }
}
