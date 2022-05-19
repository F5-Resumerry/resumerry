package com.f5.resumerry.common.service;

import com.f5.resumerry.common.dto.CommonDto;
import com.f5.resumerry.common.dto.ResponseCode;
import com.f5.resumerry.common.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public <T> ResponseDto<T> getResponseDto(T data) {
        ResponseDto<T> result = new ResponseDto<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    private void setSuccessResult(CommonDto result) {
        result.setSuccess(true);
        result.setCode(ResponseCode.SUCCESS.getCode());
        result.setMessage(ResponseCode.SUCCESS.getMessage());
    }
}
