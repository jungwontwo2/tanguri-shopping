package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.domain.entity.OrderItem;
import com.tanguri.shopping.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Orders o join fetch o.product where o.user.id = :userId")
    List<Order> findAllByUserId(@Param("userId")Long userId);

    @Query("select o from Orders o where o.product.user.id = :userId")
    List<Order> findOrderItemByProductId(@Param("userId")Long userId);


}
