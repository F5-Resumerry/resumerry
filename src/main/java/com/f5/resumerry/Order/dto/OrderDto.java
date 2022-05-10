package com.f5.resumerry.Order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String payType;
    private Long amount;
    private String cardCompany;
    private String cardNumber;
    private String cardReceiptUrl;
    private String orderId;
    private String orderName;
    private String clientEmail;
    private String clientName;
    private String paymentKey;
    private String paySuccessYn;
    private String payFailReason;
    private LocalDateTime createdDate;
}
