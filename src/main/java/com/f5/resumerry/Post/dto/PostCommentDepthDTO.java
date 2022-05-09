package com.f5.resumerry.Post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

public class PostCommentDepthDTO {
    private Long memberId;
    private String imageSrc;
    private String nickname;
    private String contents;
    private Integer recommendCnt;
    private Integer banCnt;
    private Boolean isAnonymous;
    private LocalDateTime modifiedDate;

    public PostCommentDepthDTO(Long memberId, String imageSrc, String nickname, String contents, Integer recommendCnt, Integer banCnt, Boolean isAnonymous, LocalDateTime modifiedDate) {
        this.memberId = memberId;
        this.imageSrc = imageSrc;
        this.nickname = nickname;
        this.contents = contents;
        this.recommendCnt = recommendCnt;
        this.banCnt = banCnt;
        this.isAnonymous = isAnonymous;
        this.modifiedDate = modifiedDate;
    }
}
