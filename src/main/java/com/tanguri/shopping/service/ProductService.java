package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.product.AddProductDto;
import com.tanguri.shopping.domain.entity.Image;
import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.ImageRepository;
import com.tanguri.shopping.repository.ProductRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    public Long uploadProduct(AddProductDto addProductDto,Long id) throws IOException {
        User user = userRepository.findById(id).orElse(null);
        Product product = AddProductDto.ProductDtoToProduct(addProductDto,user);
        user.getProducts().add(product);
        productRepository.save(product);

        MultipartFile image = addProductDto.getImage();
        String originalFilename = image.getOriginalFilename();
        String imgName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        // UUID 를 이용하여 파일명 새로 생성
        // UUID - 서로 다른 객체들을 구별하기 위한 클래스
        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + "_" + originalFilename; // 파일명 -> imgName

        imgName = savedFileName;

        File saveFile = new File(projectPath, imgName);
        image.transferTo(saveFile);
        Image imageBuilder = Image.builder()
                .image_url(projectPath)
                .original_image_name(originalFilename)
                .uuid_image_name(savedFileName)
                        .build();
        imageRepository.save(imageBuilder);

        return product.getId();
    }
}
