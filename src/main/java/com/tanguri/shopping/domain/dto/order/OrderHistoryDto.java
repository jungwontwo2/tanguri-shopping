package com.tanguri.shopping.domain.dto.order;

import com.tanguri.shopping.domain.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderHistoryDto {
    private String productName;
    private String productCount;
    private String productPrice;
    private String totalPrice;
    private LocalDateTime orderDate;
    private Status status;
}
