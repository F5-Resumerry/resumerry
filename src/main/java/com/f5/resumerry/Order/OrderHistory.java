package com.f5.resumerry.Order;

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
        name = "order_history"
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistory {
    @Id
    @GeneratedValue
    @Column(name = "order_history_id")
    private Long id;

    @Column(name = "order_status", nullable = false)
    private String orderStatus;

    @CreatedDate
    @Column(name = "order_history_date", nullable = false)
    private LocalDateTime orderHistoryDate;

    @OneToOne(mappedBy = "orderHistory")
    private Order order;
}