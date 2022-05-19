package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.Resume.ResumeRecommend;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeRecommendDTO {

    private Long id;

    private Long resumeId;

    private Long memberId;

    @Builder
    public ResumeRecommendDTO(Long id, Long resumeId, Long memberId){
        this.id = id;
        this.resumeId = resumeId;
        this.memberId = memberId;
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
