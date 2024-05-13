package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.product.AddProductDto;
import com.tanguri.shopping.domain.dto.product.PagingProductDto;
import com.tanguri.shopping.domain.entity.Image;
import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.ImageRepository;
import com.tanguri.shopping.repository.ProductRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        String projectPath = "C:/Temp/productImage/";

        // UUID 를 이용하여 파일명 새로 생성
        // UUID - 서로 다른 객체들을 구별하기 위한 클래스
        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + "_" + originalFilename; // 파일명 -> imgName

        imgName = savedFileName;

        File saveFile = new File(projectPath, imgName);
        image.transferTo(saveFile);
        Image imageBuilder = Image.builder()
                .image_url("/files/"+savedFileName)
                .original_image_name(originalFilename)
                .uuid_image_name(savedFileName)
                .product(product)
                        .build();
        imageRepository.save(imageBuilder);

        return product.getId();
    }
    public Page<PagingProductDto> getAllProducts(Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageLimit= 12;
        PageRequest pageRequest = PageRequest.of(page, pageLimit);
        Page<Product> products = productRepository.findAll(pageRequest);
//        productRepository.findal
//        List<Product> all = productRepository.findAll();
//        for (Product product : all) {
//            System.out.println("product.getName() = " + product.getName());
//        }
//        System.out.println("products.getTotalPages() = " + products.getTotalPages());
//        System.out.println("products.getTotalElements() = " + products.getTotalElements());
//        System.out.println("products.getNumber() = " + products.getNumber());
//        System.out.println("products.getSize() = " + products.getSize());
//        System.out.println("products.hasNext() = " + products.hasNext());
//        System.out.println("products.isFirst() = " + products.isFirst());
//        System.out.println("products.isEmpty() = " + products.isEmpty());
        Page<PagingProductDto> pagingProductDtos = products.map(product -> new PagingProductDto(product));
//        for (PagingProductDto pagingProductDto : pagingProductDtos) {
//            System.out.println("pagingProductDto.getName() = " + pagingProductDto.getName());
//        }
        return pagingProductDtos;
    }
}
