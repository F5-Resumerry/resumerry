package com.f5.resumerry.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS("SUCCESS", "성공"),
    FAIL("FAIL", "실패");

    private String code;
    private String message;
}
