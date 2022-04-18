package com.f5.resumerry.domain.entity.Order;

import com.f5.resumerry.domain.entity.Member;
import com.f5.resumerry.domain.entity.MemberInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "orders"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(name = "card_number", nullable = false)
    private Long cardNumber;

    @Column(name = "expiration_year", nullable = false)
    private Integer expirationYear;

    @Column(name = "expiration_month", nullable = false)
    private Integer expirationMonth;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "tax_free_amount", nullable = false)
    private Integer taxFreeAmount;

    @Column(name = "billing_key", nullable = false)
    private String billingKey;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @Column(name = "order_token", nullable = false)
    private String orderToken;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_member_order"))
    private Member member;

    @OneToOne
    @JoinColumn(name = "order_history_id", foreignKey = @ForeignKey(name = "FK_orderhistory_order"))
    private OrderHistory orderHistory;
}
