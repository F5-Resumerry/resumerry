package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeScrap;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ViewResumeDTO {

    private Long memberId;
    private String imageSrc;
    private String nickname;
    private String title;
    private CategoryEnum category;
    private String contents;
    private String hashtag;
    private Integer years;
    private LocalDateTime modifiedDate;
    private Long resumeId;
    private Integer recommendCnt;
    private Integer commentCnt;
    private Integer viewCnt;
    private String fileLink;
    private Boolean isOwner;
    private Boolean isScrap;
    private Boolean isRecommend;

    public ViewResumeDTO(Resume r, Boolean isOwner, Boolean isScrap, Boolean isRecommend) {
        this.memberId = r.getMemberId();
        this.imageSrc = r.getMember().getImageSrc();
        this.nickname = r.getMember().getNickname();
        this.title = r.getTitle();
        this.category = r.getCategory();
        this.contents = r.getContents();
        //this.hashtag = r.gethashtag;
        this.years = r.getYears();
        this.modifiedDate = r.getModifiedDate();
        this.resumeId = r.getId();
        this.recommendCnt = r.getResumeRecommendList().size();
        this.commentCnt = r.getResumeCommentList().size();
        this.viewCnt = r.getViewCnt();
        this.fileLink = r.getFileLink();
        this.isOwner = isOwner;
        this.isScrap = isScrap;
        this.isRecommend = isRecommend;
    }

}
