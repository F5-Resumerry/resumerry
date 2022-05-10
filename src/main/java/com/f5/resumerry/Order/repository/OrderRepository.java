package com.f5.resumerry.Order.repository;

import com.f5.resumerry.Order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
