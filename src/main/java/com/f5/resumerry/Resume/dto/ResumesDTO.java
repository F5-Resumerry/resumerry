package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.ResumeComment;
import com.f5.resumerry.Resume.ResumeHashtag;
import com.f5.resumerry.Resume.ResumeRecommend;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResumesDTO {
    //entity 값들
    private Long resumeId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private CategoryEnum category;
    private String content;
    private String fileLink;
    private String title;
    private Integer viewCnt;
    private Integer years;
    private Long memberId;
    private Boolean isDelete;
    private Boolean isLock;

    //list 들
    private List<ResumeComment> resumeCommentList;
    private List<ResumeRecommend> resumeRecommendList;
    private List<ResumeHashtag> resumeHashtagList;

    //member
    private Member member;
}
