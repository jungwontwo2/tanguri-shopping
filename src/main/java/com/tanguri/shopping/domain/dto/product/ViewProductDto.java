package com.tanguri.shopping.domain.dto.product;

import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import lombok.Data;

@Data
public class ViewProductDto {

    private Long id;
    private String name;
    private String detail;
    private Integer price;
    private Integer stock;
    private String imageUrl;

    private User user;

    public ViewProductDto(Product product){
        this.id = product.getId();
        this.name=product.getName();
        this.detail = product.getDetail();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.imageUrl = product.getImage().getImage_url();
        this.user=product.getUser();
    }
}
