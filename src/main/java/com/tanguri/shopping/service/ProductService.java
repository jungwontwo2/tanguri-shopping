package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.error.CustomException;
import com.tanguri.shopping.domain.dto.error.ErrorCode;
import com.tanguri.shopping.domain.dto.product.*;
import com.tanguri.shopping.domain.entity.*;
import com.tanguri.shopping.domain.enums.Status;
import com.tanguri.shopping.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    public Long uploadProduct(AddProductDto addProductDto,Long id) throws IOException {
        User user = userRepository.findById(id).orElse(null);
        Product product = AddProductDto.ProductDtoToProduct(addProductDto,user);
        user.getProducts().add(product);
        productRepository.save(product);

        Image imageBuilder = getImage(addProductDto.getImage(), product);
        imageRepository.save(imageBuilder);

        return product.getId();
    }

    private static Image getImage(MultipartFile image, Product product) throws IOException {
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
        return imageBuilder;
    }

    public Page<PagingProductDto> getAllProducts(Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageLimit= 12;
        PageRequest pageRequest = PageRequest.of(page, pageLimit);
        Page<Product> products = productRepository.findAll(pageRequest);
        Page<PagingProductDto> pagingProductDtos = products.map(product -> new PagingProductDto(product));

        return pagingProductDtos;
    }

    public Page<PagingProductDto> getProductsBySearch(Pageable pageable,String search){
        int page = pageable.getPageNumber()-1;
        int pageLimit=12;
        PageRequest pageRequest = PageRequest.of(page, pageLimit, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.findBySearch(pageRequest, search);
        Page<PagingProductDto> pagingProductDtos = products.map(product -> new PagingProductDto(product));
        return pagingProductDtos;
    }
    public Page<PagingProductDto> getAllPopularProducts(Pageable pageable){
        int page = pageable.getPageNumber()-1;
        int pageLimit= 12;
        PageRequest pageRequest = PageRequest.of(page, pageLimit);
        Page<Product> products = productRepository.findAllPopular(pageRequest);
        Page<PagingProductDto> pagingProductDtos = products.map(product -> new PagingProductDto(product));

        return pagingProductDtos;
    }

    public Page<PagingProductDto> getAllPopularProductsBySearch(Pageable pageable, String search) {
        int page = pageable.getPageNumber()-1;
        int pageLimit= 12;
        PageRequest pageRequest = PageRequest.of(page, pageLimit);
        Page<Product> products = productRepository.findAllPopularProductsBySearch(pageRequest,search);
        Page<PagingProductDto> pagingProductDtos = products.map(product -> new PagingProductDto(product));
        return pagingProductDtos;
    }
    public ViewProductDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return new ViewProductDto(product);
    }
    public ModifyProductDto getModifyProductDto(Long id){
        Product product = productRepository.findById(id).orElse(null);
        System.out.println("product.getId() = " + product.getId());
        ModifyProductDto modifyProductDto = new ModifyProductDto(product);
        System.out.println("modifyProductDto.getId() = " + modifyProductDto.getId());
        return modifyProductDto;
    }

    public Long productInCart(Long productId,Long userId,BuyOrCartProductDto buyOrCartProductDto){
        Product product = productRepository.findById(productId).orElse(null);
        //product.decreaseStock(buyOrCartProductDto.getCount());
        User user = userRepository.findById(userId).orElse(null);
        //user.useMoney(buyOrCartProductDto.getCount()*product.getPrice());
        CartItem cartItem = CartItem.builder()
                .cart(user.getCart())
                .count(buyOrCartProductDto.getCount())
                .product(product)
                .build();
        cartItemRepository.save(cartItem);
        return cartItem.getId();
    }

    public void modifyProduct(Long id, ModifyProductDto modifyProductDto) throws IOException {
        Product product = productRepository.findById(id).orElse(null);
        Image productImage = product.getImage();
        imageRepository.delete(productImage);
        Image image = getImage(modifyProductDto.getImgFile(), product);
        imageRepository.save(image);
        product.modifyProduct(modifyProductDto,image);
        productRepository.save(product);
    }

    public void deleteProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if(product.getUser().getId()!=userId){
            throw new CustomException(ErrorCode.PRODUCT_OWNER);
        }
        else {
            productRepository.delete(product);
        }
    }


}
