package com.tanguri.shopping.domain.entity;

import com.tanguri.shopping.domain.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String city;
    private String street;
    private String zipcode;
}
