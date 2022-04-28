package com.f5.resumerry.Member.dto;

import com.f5.resumerry.Member.entity.MemberInfo;
import com.f5.resumerry.converter.BooleanToYNConverter;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberInfoDTO {

    private Long id;

    private Integer token;

    private Integer stack;

    private Boolean emailVerified;

    private String imageSrcS;

    private String imageSrcM;

    private String imageSrcL;

    @Builder
    public MemberInfoDTO(Long id) {
        this.id = id;
        this.token = 0;
        this.stack = 0;
        this.emailVerified = true;
        this.imageSrcS = "imageSrcS";
        this.imageSrcM = "imageSrcM";
        this.imageSrcL = "imageSrcL";
    }

    public MemberInfo toEntity(){
        MemberInfo build = MemberInfo.builder()
                .id(id)
                .token(token)
                .stack(stack)
                .emailVerified(emailVerified)
                .imageSrcS(imageSrcS)
                .imageSrcM(imageSrcM)
                .imageSrcL(imageSrcL)
                .build();
        return build;
    }
}
