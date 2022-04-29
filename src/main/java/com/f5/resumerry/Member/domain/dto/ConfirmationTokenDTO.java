package com.f5.resumerry.Member.domain.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConfirmationTokenDTO {

    private String id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String receiverEmail;

    @Builder
    public ConfirmationTokenDTO(String id, String receiverEmail) {
        this.id = id;
        this.receiverEmail = receiverEmail;
    }

}
