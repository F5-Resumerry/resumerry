package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.ResumeHashtag;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResumeHashtagDTO {

    private Long id;

    private Long resumeId;

    private Long hashtagId;

    @Builder
    public ResumeHashtagDTO(Long id, Long resumeId, Long hashtagId){
        this.id = id;
        this.resumeId = resumeId;
        this.hashtagId = hashtagId;
    }

    public ResumeHashtag toEntity(){
        ResumeHashtag build = ResumeHashtag.builder()
                .id(id)
                .resumeId(resumeId)
                .hashtagId(hashtagId)
                .build();
        return build;
    }
}