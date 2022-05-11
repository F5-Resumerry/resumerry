package com.f5.resumerry.Order.entity;

import com.f5.resumerry.Member.domain.entity.Member;
import com.f5.resumerry.Order.dto.OrderDto;
import com.f5.resumerry.Order.dto.OrderResponseDto;
import com.f5.resumerry.Order.dto.PayType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String orderName;

    @Setter
    @Column
    private String cardCompany;			// 카드회사

    @Setter
    @Column
    private String cardNumber;			// "949129******7058"

    @Setter
    @Column
    private String cardReceiptUrl;		// 영수증 링크

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Setter
    @Column
    private String paymentKey;

    @Setter
    @Column(nullable = false)
    private String paySuccessYn;

    @Setter
    @Column
    private String payFailReason;

    @Setter
    @Column(nullable = false)
    private String cancelYn;				// 결제 취소 여부

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Member client;

    public OrderResponseDto toRes() {
        return OrderResponseDto.builder()
                .payType(payType.getName())
                .amount(amount)
                .orderId(orderId)
                .orderName(orderName)
                .clientEmail(clientEmail)
                .clientName(clientName)
                .createdDate(createdDate)
                .build();
    }

    public OrderDto toDto() {
        return OrderDto.builder()
                .id(id)
                .payType(payType.getName())
                .amount(amount)
                .cardCompany(cardCompany)
                .cardNumber(cardNumber)
                .cardReceiptUrl(cardReceiptUrl)
                .orderId(orderId)
                .orderName(orderName)
                .clientEmail(clientEmail)
                .clientName(clientName)
                .paymentKey(paymentKey)
                .paySuccessYn(paySuccessYn)
                .payFailReason(payFailReason)
                .createdDate(createdDate)
                .build();
    }
}
