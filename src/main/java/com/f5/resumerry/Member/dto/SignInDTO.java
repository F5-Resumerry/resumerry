package com.f5.resumerry.Member.dto;
import com.f5.resumerry.Member.entity.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignInDTO {


    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String accountName;


    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;



    @Builder
    public SignInDTO(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public Member toEntity(){
        Member build = Member.builder()
                .accountName(accountName)
                .password(password)
                .build();
        return build;
    }
}