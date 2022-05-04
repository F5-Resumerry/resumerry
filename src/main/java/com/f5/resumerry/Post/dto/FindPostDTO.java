package com.f5.resumerry.Post.dto;

import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FindPostDTO {

    private Long postId;
    private String title;
    private String contents;
    private Integer commentCnt;
    private Integer viewCnt;
    private Boolean isAnonymous;
    private String imageSrc;
    private Long memberId;
    private String nickname;
    private LocalDateTime modifiedDate;
    private CategoryEnum category;




    public FindPostDTO(Long postId, String title, String contents, Integer commentCnt, Integer viewCnt, Boolean isAnonymous, String imageSrc, Long memberId, String nickname, LocalDateTime modifiedDate, CategoryEnum category) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.commentCnt = commentCnt;
        this.viewCnt = viewCnt;
        this.isAnonymous = isAnonymous;
        this.imageSrc = imageSrc;
        this.memberId = memberId;
        this.nickname = nickname;
        this.modifiedDate = modifiedDate;
        this.category = category;
    }

}
