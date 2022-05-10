package com.f5.resumerry.Order.controller;

import com.f5.resumerry.Order.dto.OrderRequestDto;
import com.f5.resumerry.Order.dto.OrderResponseDto;
import com.f5.resumerry.Order.service.OrderService;
import com.f5.resumerry.common.dto.ResponseDto;
import com.f5.resumerry.common.service.CommonService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final CommonService commonService;

    @PostMapping
    @ApiOperation(value = "결제 요청", notes = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseDto<OrderResponseDto> requestPayments(
            @ApiParam(value = "요청 객체", required = true) @ModelAttribute OrderRequestDto paymentReq
    ) throws Exception {
        try {
            return commonService.getResponseDto(
                    orderService.requestPayments(paymentReq)
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
