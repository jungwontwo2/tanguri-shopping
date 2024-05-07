package com.tanguri.shopping.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
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
    private String name;
    private String addressNumber;
    private String address;
    private String detailAddress;
    private String phone;
    private String role;
    private Integer money;
    private LocalDateTime createDate;

    public User(String username,String password,String email,String name,
                String addressNumber,String address,String detailAddress,String phone,String role){
        this.username=username;
        this.password=password;
        this.email=email;
        this.name=name;
        this.address = address;
        this.addressNumber = addressNumber;
        this.detailAddress =detailAddress;
        this.phone=phone;
        this.role=role;
    }
}
