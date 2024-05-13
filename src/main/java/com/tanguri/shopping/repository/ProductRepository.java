package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select p from Product p join fetch p.image")
    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p join fetch p.image where p.id = :id")
    Optional<Product> findById(@Param("id") Long id);
}
