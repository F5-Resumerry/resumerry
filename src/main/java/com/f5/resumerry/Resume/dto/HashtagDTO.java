package com.f5.resumerry.Resume.dto;

import com.f5.resumerry.Resume.Hashtag;
import com.f5.resumerry.Resume.Resume;
import com.f5.resumerry.selector.CategoryEnum;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HashtagDTO {

    private Long id;

    private String hashtagName;


    @Builder
    public HashtagDTO(Long id, String hashtagName){
        this.id = id;
        this.hashtagName = hashtagName;

    }

    public Hashtag toEntity(){
        Hashtag build = Hashtag.builder()
                .id(id)
                .hashtagName(hashtagName)
                .build();
        return build;
    }
}