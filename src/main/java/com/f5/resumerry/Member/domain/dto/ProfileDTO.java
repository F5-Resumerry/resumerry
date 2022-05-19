package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.selector.CategoryEnum;
import com.f5.resumerry.selector.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {

    private Long memberId;
    private String accountName;
    private String nickname;
    private Integer years;
    private CategoryEnum category;
    private String email;
    private String introduce;
    private Role role;
    private String imageSrc;
    private Integer stack;
    private Integer token;

}
