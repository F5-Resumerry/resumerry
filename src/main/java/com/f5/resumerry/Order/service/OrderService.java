package com.f5.resumerry.Order.service;

import com.f5.resumerry.Member.repository.MemberRepository;
import com.f5.resumerry.Order.dto.OrderResponseHandleDto;
import com.f5.resumerry.Order.dto.PayType;
import com.f5.resumerry.Order.entity.Order;
import com.f5.resumerry.Order.dto.OrderRequest;
import com.f5.resumerry.Order.dto.OrderResponseDto;
import com.f5.resumerry.Order.repository.OrderRepository;
import com.f5.resumerry.exception.BusinessException;
import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Service
public class OrderService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Value("${payments.toss.client-api-key}")
    private String testClientApiKey;

    @Value("${payments.toss.secret-api-key}")
    private String testSecretApiKey;

    @Value("${payments.toss.success-url}")
    private String successCallBackUrl;

    @Value("${payments.toss.fail-url}")
    private String failCallBackUrl;

    @Value("${payments.toss.origin-url}")
    private String tossOriginUrl;

    @Transactional
    public OrderResponseDto requestPayments(OrderRequest orderRequest) {
        Long amount = orderRequest.getAmount();
        String payType = orderRequest.getPayType().getName();
        String clientEmail = orderRequest.getClientEmail();
        String orderName = orderRequest.getOrderName();

        if (!payType.equals("가상계좌") && !payType.equals("카드")) {
            throw new BusinessException("잘못된 결제타입");
        }

        OrderResponseDto orderResponseDto;
        try {
            Order order = orderRequest.toEntity();
            memberRepository.findByEmail(clientEmail)
                    .ifPresentOrElse(
                            M -> M.addOrder(order)
                            , () -> {
                                throw new BusinessException("존재하지 않는 회원입니다.");
                            });
            orderResponseDto = order.toRes();
            System.out.println(successCallBackUrl);
            orderResponseDto.setSuccessUrl(successCallBackUrl);
            orderResponseDto.setFailUrl(failCallBackUrl);

            return orderResponseDto;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Transactional
    public OrderResponseHandleDto sendPaymentApproveRequest(String paymentKey, Long amount, String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException("주문이 없습니다."));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        String encodedAuth = new String(Base64.getEncoder().encode((testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8)));

        httpHeaders.setBasicAuth(encodedAuth);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject parameter = new JSONObject();
        parameter.put("orderId", orderId);
        parameter.put("amount", amount);

        OrderResponseHandleDto orderResponseHandleDto;

        try {
            orderResponseHandleDto = restTemplate.postForEntity(
                    tossOriginUrl + "/payments/" + paymentKey,
                    new HttpEntity<>(parameter, httpHeaders),
                    OrderResponseHandleDto.class
            ).getBody();

            if (orderResponseHandleDto ==  null) {
                throw new BusinessException("잘못된 주문입니다.");
            } else {
                memberRepository.findByEmail(order.getClientEmail())
                        .ifPresentOrElse(
                                M -> M.addToken(100)
                                , () -> {
                                    throw new BusinessException("존재하지 않는 회원입니다.");
                                });
            }
        } catch (Exception e) {
            throw new BusinessException("최종결제 실행 요청에 실패하였습니다.");
        }

        // ToDo 비즈니스 로직 실행
        return orderResponseHandleDto;
    }
}
