package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    private String username;

    private String password;

    private String email;

    private String address;

    private String phone;

    private String role;

    private Integer money;

    private LocalDateTime createDate;
}
