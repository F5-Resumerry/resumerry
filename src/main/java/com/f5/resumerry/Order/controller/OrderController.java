package com.f5.resumerry.Order.controller;

import com.f5.resumerry.Order.dto.OrderRequest;
import com.f5.resumerry.Order.dto.OrderResponseDto;
import com.f5.resumerry.Order.dto.OrderResponseHandleDto;
import com.f5.resumerry.Order.service.OrderService;
import com.f5.resumerry.common.dto.ResponseDto;
import com.f5.resumerry.common.service.CommonService;
import com.f5.resumerry.exception.BusinessException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class OrderController {

    private final OrderService orderService;
    private final CommonService commonService;

    @PostMapping
    @ApiOperation(
            value = "결제 요청", notes = "결제 요청에 필요한 값들을 반환합니다.")
    public ResponseDto<OrderResponseDto> requestPayments(
            @ApiParam(value = "요청 객체", required = true) @ModelAttribute OrderRequest paymentReq
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

    @GetMapping("/success")
    @ApiOperation(value = "결제 설공 콜백")
    public ResponseDto<OrderResponseHandleDto> receivePaymentSuccessCallback(
            @ApiParam(value = "토스's 결제 고유 번호", required = true) @RequestParam String paymentKey,
            @ApiParam(value = "레쥬메리's 결제 고유 번호", required = true) @RequestParam String orderId,
            @ApiParam(value = "실결제 금액", required = true) @RequestParam Long amount
    ) {
        try {
            OrderResponseHandleDto result = orderService.sendPaymentApproveRequest(paymentKey, amount, orderId);

            return commonService.getResponseDto(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(e.getMessage());
        }
    }
}
