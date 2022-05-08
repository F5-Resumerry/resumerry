package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NicknameDTO {


    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Builder
    public NicknameDTO(String nickname) {
        this.nickname = nickname;
    }

    public Member toEntity(){
        Member build = Member.builder()
                .nickname(nickname)
                .build();
        return build;
    }
}