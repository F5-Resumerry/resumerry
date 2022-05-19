package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterViewResumeDTO {
    private Long resumeId;
    private String title;
    private String contents;
    private Integer recommendCnt;
    private Integer commentCnt;
    private Integer viewCnt;
    private LocalDateTime modifiedDate;
    private List<ResumeHashtag> hashtag;
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private Integer years;
    public FilterViewResumeDTO(Resume r) {
        this.resumeId = r.getId();
        this.title = r.getTitle();
        this.contents = r.getContents();
        this.recommendCnt = r.getResumeRecommendList().size();
        this.commentCnt = r.getResumeCommentList().size();
        this.viewCnt = r.getViewCnt();
        this.modifiedDate = r.getModifiedDate();
        this.hashtag = r.getResumeHashtagList();
        this.memberId = r.getMemberId();
        this.imageSrc = r.getMember().getImageSrc();
        this.nickname = r.getMember().getNickname();
        this.years = r.getYears();
    }
}
