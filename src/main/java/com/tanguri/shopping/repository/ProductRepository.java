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

    @Query("select p from Product p join fetch p.image order by p.id desc")
    Page<Product> findAll(Pageable pageable);

    @Query("select p from Product p join fetch p.image join fetch p.user where p.id = :id order by p.id desc")
    Optional<Product> findById(@Param("id") Long id);

    @Query("select p from Product p join fetch p.image where p.name like %:search% order by p.id desc")
    Page<Product> findBySearch(Pageable pageable,@Param("search") String search);

    @Query("select p from Product p join fetch p.image order by p.soldProductCount desc")
    Page<Product> findAllPopular(Pageable pageable);

    @Query("select p from Product p join fetch p.image where p.name like %:search% order by p.soldProductCount desc")
    Page<Product> findAllPopularProductsBySearch(Pageable pageable,@Param("search")String search);
}
