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

    @OneToOne(mappedBy = "product")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    private Integer stock;
    private String detail;
    private Integer price;
    private String name;
}
