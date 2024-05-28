package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.product.id = :id")
    Image findByProductId(@Param("id") Long id);
}
