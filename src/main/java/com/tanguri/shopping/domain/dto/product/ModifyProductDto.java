package com.tanguri.shopping.domain.dto.product;

import com.tanguri.shopping.domain.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProductDto {
    private Long id;
    @NotEmpty(message = "이름을 입력해주세요")
    private String name;
    @NotEmpty(message = "상품 설명을 입력해주세요")
    private String detail;
    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;
    @NotNull(message = "재고를 입력해주세요")
    private Integer stock;
    @NotNull(message = "이미지를 업로드해주세요")
    private MultipartFile imgFile;

    public ModifyProductDto(Product product) {
        id = product.getId();
        name = product.getName();
        detail = product.getDetail();
        price = product.getPrice();
        stock = product.getStock();
    }
}
