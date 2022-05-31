package com.f5.resumerry.Resume.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeDTO {
    private Long resumeId;
    private String title;
    private String contents;
    private Integer recommendCnt;
    private Integer commentCnt;
    private Integer viewCnt;
    private LocalDateTime modifiedDate;
   // private List<String> Hashtag;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private Integer years;
}
