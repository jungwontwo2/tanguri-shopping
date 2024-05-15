package com.tanguri.shopping.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY)
    private Image image;
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "image_id")
//    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<CartItem> cartItems;

    private Integer stock;
    private String detail;
    private Integer price;
    private String name;
    public void decreaseStock(Integer count){
        stock-=count;
    }
}
