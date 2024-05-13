package com.tanguri.shopping.domain.dto.product;

import com.tanguri.shopping.domain.entity.Image;
import com.tanguri.shopping.domain.entity.Product;
import lombok.Data;

@Data
public class PagingProductDto {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String status;

    public PagingProductDto(Product product){
        this.id=product.getId();
        this.name=product.getName();
        this.imageUrl=product.getImage().getImage_url();
        this.price=product.getPrice();
        if(product.getStock()==0){
            this.status="품절";
        }
        else if(product.getStock()<=10){
            this.status="품절임박";
        }
        else
            this.status="판매중";
    }
}
