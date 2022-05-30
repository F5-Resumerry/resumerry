package com.f5.resumerry.Order.dto;

import com.f5.resumerry.Order.entity.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @ApiModelProperty("지불방법")
    private PayType payType;

    @ApiModelProperty("지불금액")
    private Long amount;

    @ApiModelProperty("주문 이름")
    private String orderName;

    @ApiModelProperty("구매자 이름")
    private String clientName;

    @ApiModelProperty("구매자 이메일")
    private String clientEmail;

    public Order toEntity() {
        return Order.builder()
                .orderId(UUID.randomUUID().toString())
                .payType(payType)
                .amount(amount)
                .orderName(orderName)
                .clientName(clientName)
                .clientEmail(clientEmail)
                .paySuccessYn("N")
                .createdDate(LocalDateTime.now())
                .build();
    }
}
