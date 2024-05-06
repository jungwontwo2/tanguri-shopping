package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String uuid_image_name;
    private String image_url;
    private String original_image_name;
}
