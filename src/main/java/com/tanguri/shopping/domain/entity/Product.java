package com.tanguri.shopping.domain.entity;


import com.tanguri.shopping.domain.dto.product.ModifyProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToOne(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Rating> ratings;
    private Integer soldProductCount;
    private Integer stock;
    private String detail;
    private Integer price;
    private String name;
    public void decreaseStock(Integer count){
        stock-=count;
    }
    public void modifyProduct(ModifyProductDto modifyProductDto, Image image){
        this.name=modifyProductDto.getName();
        this.detail = modifyProductDto.getDetail();
        this.price = modifyProductDto.getPrice();
        this.stock = modifyProductDto.getStock();
        this.image=image;
    }

    public void addSoldProductCount(Integer count) {
        soldProductCount+=count;
    }

    public void minusProductCount(Integer count) {
        soldProductCount-=count;
    }
}
