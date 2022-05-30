package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.Hashtag;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeHashtag;
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
    private List<ResumeHashtag> hashtag;
    private Boolean isLock;
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
        //this.hashtag = r.getResumeHashtagList();
        this.memberId = r.getMemberId();
        this.imageSrc = r.getMember().getImageSrc();
        this.nickname = r.getMember().getNickname();
        this.years = r.getYears();
        this.isLock = r.getIsLock();
    }

    public static FilterViewResumeDTO of(ResumesDTO r) {
        return new FilterViewResumeDTO(
                r.getResumeId(),
                r.getTitle(),
                r.getContent(),
                r.getResumeRecommendList().size(),
                r.getResumeCommentList().size(),
                r.getViewCnt(),
                r.getModifiedDate(),
                r.getResumeHashtagList(),
                r.getIsLock(),
                r.getMemberId(),
                r.getMember().getImageSrc(),
                r.getMember().getNickname(),
                r.getYears()
        );
    }


}
