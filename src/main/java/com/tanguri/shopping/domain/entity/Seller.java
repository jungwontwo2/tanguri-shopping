package com.tanguri.shopping.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();

    private Integer earning;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime localDateTime;
}
