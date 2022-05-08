package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.Member.domain.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccountNameDTO {


    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String accountName;

    @Builder
    public AccountNameDTO(String accountName) {
        this.accountName = accountName;
    }

    public Member toEntity(){
        Member build = Member.builder()
                .accountName(accountName)
                .build();
        return build;
    }
}