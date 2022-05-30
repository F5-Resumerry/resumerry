package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeRecommendDTO {

    private Long id;
    private Long resumeId;
    private Long memberId;
    private String title;
    private String fileLink;
    private String contents;
    private LocalDateTime createdDate;

    @Builder
    public ResumeRecommendDTO(Long id, Long resumeId, Long memberId){
        this.id = id;
        this.resumeId = resumeId;
        this.memberId = memberId;
    }

    @Builder
    public ResumeRecommendDTO(Long resumeId, String title, String fileLink, String contents, LocalDateTime createdDate) {
        this.resumeId = resumeId;
        this.title = title;
        this.fileLink = fileLink;
        this.contents = contents;
        this.createdDate = createdDate;
    }

    public ResumeRecommend toEntity(){
        ResumeRecommend build = ResumeRecommend.builder()
                .id(id)
                .resumeId(resumeId)
                .memberId(memberId)
                .build();
        return build;
    }
}
