package com.f5.resumerry.Member.domain.dto;

import com.f5.resumerry.selector.CategoryEnum;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmendRequestDTO {
    @ApiParam("닉네임") String nickname;
    @ApiParam("연차")  Integer years;
    @ApiParam("카테고리") CategoryEnum category;
    @ApiParam("소개글") String introduce;
    @ApiParam("재직 여부")  Boolean isWorking;
}
