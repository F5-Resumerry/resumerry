package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.selector.CategoryEnum;
import com.f5.resumerry.selector.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReqDTO {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$"
            , message = "아이디는 영소문자와 숫자를 포함한 4자 ~ 12자의 아이디여야 합니다.")
    private String accountName;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$"
            , message = "닉네임은 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성된 닉네임이여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"]).{8,20}"
            , message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotNull(message = "연차는 필수 입력 값입니다.")
    private Integer years;

    @NotNull(message = "재직 여부는 필수 입력 값입니다.")
    private Boolean isWorking;

    @NotNull(message = "역할은 필수 입력 값입니다.")
    private Role role;

    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private CategoryEnum category;
}
