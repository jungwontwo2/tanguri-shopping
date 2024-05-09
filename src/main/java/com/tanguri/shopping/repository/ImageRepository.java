package com.tanguri.shopping.repository;

import com.tanguri.shopping.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
