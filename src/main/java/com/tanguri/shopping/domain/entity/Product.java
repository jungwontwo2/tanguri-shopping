package com.tanguri.shopping.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY)
    private CartItem cartItem;

    private Integer stock;
    private String detail;
    private Integer price;
    private String name;
}