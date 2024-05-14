package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
