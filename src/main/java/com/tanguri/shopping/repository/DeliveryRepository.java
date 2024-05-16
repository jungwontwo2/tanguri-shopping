package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("select d from Delivery d where d.order.id = :orderId")
    Delivery findByOrderItemId(Long orderId);
}
