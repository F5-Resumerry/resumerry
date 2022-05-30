package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.ResumeScrap;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeScrapDTO {

    private Long id;

    private Long resumeId;

    private Long memberId;

    private LocalDateTime createdDate;

    @Builder
    public ResumeScrapDTO(Long id, Long resumeId, Long memberId, java.time.LocalDateTime createdDate){
        this.id = id;
        this.resumeId = resumeId;
        this.memberId = memberId;
        this.createdDate = createdDate;
    }

    public ResumeScrap toEntity(){
        ResumeScrap build = ResumeScrap.builder()
                .id(id)
                .resumeId(resumeId)
                .memberId(memberId)
                .createdDate(createdDate)
                .build();
        return build;
    }

    public void setCreatedDate(java.time.LocalDateTime now) {
        this.createdDate = now;
    }
}
