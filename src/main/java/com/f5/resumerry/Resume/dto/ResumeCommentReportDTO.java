package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.ResumeCommentReport;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeCommentReportDTO {

    private Long id;

    private Long resumeCommentId;

    private Long memberId;

    @Builder
    public ResumeCommentReportDTO(Long id, Long resumeCommentId, Long memberId){
        this.id = id;
        this.resumeCommentId = resumeCommentId;
        this.memberId = memberId;
    }

    public ResumeCommentReport toEntity(){
        ResumeCommentReport build = ResumeCommentReport.builder()
                .id(id)
                .resumeCommentId(resumeCommentId)
                .memberId(memberId)
                .build();
        return build;
    }
}
