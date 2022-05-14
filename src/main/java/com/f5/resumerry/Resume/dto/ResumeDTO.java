package com.f5.resumerry.Resume.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeDTO {
    private Long resumeId;
    private String Title;
    private String contents;
    private Integer recommendCnt;
    private Integer commentCnt;
    private Integer viewCnt;
    private LocalDateTime modifiedDate;
//    private String Hashtag;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private Integer years;

}
