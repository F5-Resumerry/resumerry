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

    private Resume resume;

    private Member member;

    @Builder
    public ResumeRecommendDTO(Long id, Resume resume, Member member){
        this.id = id;
        this.resume = resume;
        this.member = member;
    }

    public ResumeRecommend toEntity(){
        ResumeRecommend build = ResumeRecommend.builder()
                .id(id)
                .resume(resume)
                .member(member)
                .build();
        return build;
    }
}
