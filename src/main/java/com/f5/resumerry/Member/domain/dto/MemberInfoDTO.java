package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.Member.domain.entity.MemberInfo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberInfoDTO {

    private Long id;

    private Integer token;

    private Integer stack;

    private Boolean emailVerified;

    //private String imageSrc;


    @Builder
    public MemberInfoDTO(Long id) {
        this.id = id;
        this.token = 0;
        this.stack = 0;
        this.emailVerified = true;
        //this.imageSrc = "imageSrc";

    }

    public MemberInfo toEntity(){
        MemberInfo build = MemberInfo.builder()
                .id(id)
                .token(token)
                .stack(stack)
                .emailVerified(emailVerified)
                //.imageSrc(imageSrc)
                .build();
        return build;
    }
}