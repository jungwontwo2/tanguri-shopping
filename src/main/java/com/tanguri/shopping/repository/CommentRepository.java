package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.product.id = :productId")
    List<Comment> findByProductId(@Param("productId") Long productId);

    @Query("select c from Comment c left join fetch c.children where c.product.id = :productId")
    List<Comment> findByProductIdWithChildren(@Param("productId") Long productId);
}
