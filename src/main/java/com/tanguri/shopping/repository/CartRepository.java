package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c where c.user.id = :id")
    Cart findCartByUserId(@Param("id") Long id);
}
