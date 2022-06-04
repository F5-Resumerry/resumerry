package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.Hashtag;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FilterViewResumeDTO {
    private Long resumeId;
    private String title;
    private String contents;
    private Integer recommendCnt;
    private Integer commentCnt;
    private Integer viewCnt;
    private LocalDateTime modifiedDate;
    private List<String> hashtag;
    private Boolean isLock;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private Integer years;


}
