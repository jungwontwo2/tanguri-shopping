package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("select c from CartItem c where c.cart.id = :cartId")
    List<CartItem> findAllByCartId(@Param("cartId") Long cartId);
}
