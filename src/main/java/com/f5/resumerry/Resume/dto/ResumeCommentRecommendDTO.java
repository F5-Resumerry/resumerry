package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.ResumeCommentRecommend;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeCommentRecommendDTO {

    private Long id;

    private Long resumeCommentId;

    private Long memberId;

    @Builder
    public ResumeCommentRecommendDTO(Long id, Long resumeCommentId, Long memberId){
        this.id = id;
        this.resumeCommentId = resumeCommentId;
        this.memberId = memberId;
    }

    public ResumeCommentRecommend toEntity(){
        ResumeCommentRecommend build = ResumeCommentRecommend.builder()
                .id(id)
                .resumeCommentId(resumeCommentId)
                .memberId(memberId)
                .build();
        return build;
    }
}
