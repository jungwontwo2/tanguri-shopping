package com.tanguri.shopping.domain.dto.product;

import lombok.Data;

@Data
public class BuyOrCartProductDto {
    private Integer count;

    public BuyOrCartProductDto(Integer count) {
        this.count = count;
    }
}
