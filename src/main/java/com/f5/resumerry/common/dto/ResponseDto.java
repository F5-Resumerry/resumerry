package com.f5.resumerry.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto<T> extends CommonDto {
    private T data;
}
