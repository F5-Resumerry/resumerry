package com.f5.resumerry.Member.domain.dto;
import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Member.domain.entity.MemberCategory;
import com.f5.resumerry.Member.domain.entity.MemberInfo;
import com.f5.resumerry.selector.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignUpDTO {

    private Long id;

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

//    @NotNull(message = "카테고리는 필수 입력 값입니다.")
    private List<MemberCategory> memberCategoryList = new ArrayList<>();

    @NotNull(message = "재직 여부는 필수 입력 값입니다.")
    private Boolean isWorking;

    @NotNull(message = "역할은 필수 입력 값입니다.")
    private Role role;

    private String salt;

    private String access_token;

    private String refresh_token;

    private MemberInfo memberInfo;

    @Builder
    public SignUpDTO(Long id, String accountName, String nickname, String email, String password, Integer years, List<MemberCategory> memberCategoryList, Boolean isWorking, Role role, String salt, String access_token, String refresh_token, MemberInfo memberInfo) {
        this.id = id;
        this.accountName = accountName;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.years = years;
        this.memberCategoryList = memberCategoryList;
        this.isWorking = isWorking;
        this.role = role;
        this.memberInfo = memberInfo;
        this.salt = salt;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public Member toEntity(){
        Member build = Member.builder()
                .id(id)
                .accountName(accountName)
                .nickname(nickname)
                .email(email)
                .password(password)
                .years(years)
                .memberCategoryList(memberCategoryList)
                .isWorking(isWorking)
                .role(role)
                .memberInfo(memberInfo)
                .salt(salt)
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();
        return build;
    }
}