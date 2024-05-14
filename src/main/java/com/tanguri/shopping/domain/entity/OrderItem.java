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
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")

    private Long productId;
    private String productName;
    private Integer productPrice;
    private Integer productCount;
    private Status status;
}
