package com.tanguri.shopping.domain.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuyOrCartProductDto {
    private Integer count;

    public BuyOrCartProductDto(Integer count) {
        this.count = count;
    }
}
