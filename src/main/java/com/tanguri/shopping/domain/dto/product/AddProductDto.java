package com.tanguri.shopping.domain.dto.product;

import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddProductDto {

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;
    @NotEmpty(message = "상품 설명을 입력해주세요")
    private String detail;
    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;
    @NotNull(message = "재고를 입력해주세요")
    private Integer stock;
    @NotNull(message = "이미지를 업로드해주세요")
    private MultipartFile image;

    public static Product ProductDtoToProduct(AddProductDto addProductDto, User user){
        Product product = Product.builder()
                .soldProductCount(0)
                .name(addProductDto.getName())
                .detail(addProductDto.getDetail())
                .price(addProductDto.getPrice())
                .stock(addProductDto.getStock())
                .user(user)
                .build();
        return product;
    }
}
