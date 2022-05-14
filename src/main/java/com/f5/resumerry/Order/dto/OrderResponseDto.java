package com.f5.resumerry.Order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private String payType;
    private Long amount;
    private String orderId;
    private String orderName;
    private String clientName;
    private String clientEmail;
    private String successCallbackEndpoint;
    private String failCallbackEndpoint;
    private LocalDateTime createdDate;
    private String paySuccessYn;
    private String successYn;
    private String successUrl;
    private String failUrl;
}
