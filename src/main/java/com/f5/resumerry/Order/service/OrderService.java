package com.f5.resumerry.Order.service;

import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Order.entity.Order;
import com.f5.resumerry.Order.dto.OrderRequestDto;
import com.f5.resumerry.Order.dto.OrderResponseDto;
import com.f5.resumerry.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private MemberRepository memberRepository;

    @Value("${payments.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payments.toss.test_secret_api_key}")
    private String testSecretApiKey;

    @Value("${payments.toss.success_url}")
    private String successCallBackUrl;

    @Value("${payments.toss.fail_url}")
    private String failCallBackUrl;

    @Value("${payments.toss.origin_url}")
    private String tossOriginUrl;

    @Transactional
    public OrderResponseDto requestPayments(OrderRequestDto orderRequestDto) {
        Long amount = orderRequestDto.getAmount();
        String payType = orderRequestDto.getPayType().getName();
        String clientEmail = orderRequestDto.getClientEmail();
        String orderName = orderRequestDto.getOrderName();

        if (!payType.equals("가상계좌") && !payType.equals("카드")) {
            throw new BusinessException("잘못된 결제타입");
        }

        OrderResponseDto orderResponseDto;
        try {
            Order order = orderRequestDto.toEntity();
            memberRepository.findByEmail(clientEmail)
                    .ifPresentOrElse(
                            M -> M.addOrder(order)
                            , () -> {
                                throw new BusinessException("존재하지 않는 회원입니다.");
                            });
            orderResponseDto = order.toRes();
            orderResponseDto.setSuccessUrl(successCallBackUrl);
            orderResponseDto.setFailUrl(failCallBackUrl);

            return orderResponseDto;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
