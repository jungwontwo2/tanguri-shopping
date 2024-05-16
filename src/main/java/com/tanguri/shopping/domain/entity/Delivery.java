package com.tanguri.shopping.domain.entity;

import com.tanguri.shopping.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String addressNumber;
    private String address;
    private String detailAddress;
    public void startDelivery(){
        this.status= Status.배송중;
    }

    public void completeDelivery() {
        this.status=Status.배송완료;
    }
}
