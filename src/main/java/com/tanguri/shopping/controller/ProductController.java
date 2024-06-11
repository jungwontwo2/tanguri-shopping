package com.tanguri.shopping.controller;

import com.tanguri.shopping.AuthenticationHelper;
import com.tanguri.shopping.domain.dto.comments.WriteCommentDto;
import com.tanguri.shopping.domain.dto.product.*;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.*;
import com.tanguri.shopping.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;
    private final ImageService imageService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentService commentService;

    @GetMapping("product/upload")
    public String productUploadForm(Model model, @ModelAttribute("product") AddProductDto addProductDto) {
        authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        return "user/seller/addProduct";
    }

    @PostMapping("product/upload")
    public String productUpload(@Validated @ModelAttribute("product") AddProductDto addProductDto, BindingResult bindingResult,
                                 Model model, HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()||!addProductDto.isImageValid()) {
            if(!addProductDto.isImageValid()){
                bindingResult.rejectValue("image","image.valid","이미지를 업로드해주세요.");
            }
            authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
            return "user/seller/addProduct";
        }

        Long productId = productService.uploadProduct(addProductDto, authenticationHelper.getAuthenticatedUserId());
        request.setAttribute("msg", "상품 업로드를 완료했습니다.");
        String redirectUrl = "/product/" + productId;
        request.setAttribute("redirectUrl", redirectUrl);
        return "common/messageRedirect";
    }

    @GetMapping("product/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model,
                              @ModelAttribute("buyOrCartProductDto") BuyOrCartProductDto buyOrCartProductDto,
                              @ModelAttribute("Comment")WriteCommentDto writeCommentDto) {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if(userId!=null){
            Cart cart = userService.getCartByUserId(userId);
            List<CartItem> cartItems = cart.getCartItems();
            model.addAttribute("totalProductCount", cartItems.size());
            model.addAttribute("user", userService.findUser(userId));
            model.addAttribute("currentUserId",userId);
        }
        ViewProductDto product = productService.getViewProductDto(id);
        List<Comment> comments = commentService.getCommentsByProductId(id);
        model.addAttribute("comments",comments);
        model.addAttribute("product", product);
        return "product/ProductView";
    }

    @PostMapping("product/order/{productId}")
    public String buyProduct(@PathVariable("productId") Long productId,
                             @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                             HttpServletRequest request) {
        Long id = authenticationHelper.getAuthenticatedUserId();
        Integer money = userService.getMoney(id);
        if (productService.getViewProductDto(productId).getPrice() * buyOrCartProductDto.getCount() > money) {
            request.setAttribute("msg", "잔액이 부족합니다.\n 충전 후 다시 사용바랍니다.");
            String redirectUrl = "/product/" + productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        } else if (productService.getViewProductDto(productId).getStock() < buyOrCartProductDto.getCount()) {
            request.setAttribute("msg", "재고가 부족합니다.\n최대 수량은" + productService.getViewProductDto(productId).getStock() + "개 입니다");
            String redirectUrl = "/product/" + productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        } else {
            orderService.orderProduct(productId, id, buyOrCartProductDto);
            request.setAttribute("msg", "주문을 완료했습니다.");
            String redirectUrl = "/product/"+productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
    }

    @PostMapping("product/cart/{productId}")
    public String productInCart(@PathVariable("productId") Long productId,
                                @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                                HttpServletRequest request) {
        Long id = authenticationHelper.getAuthenticatedUserId();
        productService.productInCart(productId, id, buyOrCartProductDto);
        request.setAttribute("msg", "장바구니에 상품을 담았습니다.");
        String redirectUrl = "/product/"+productId;
        request.setAttribute("redirectUrl", redirectUrl);
        return "common/messageRedirect";
    }

    @GetMapping("product/modify/{id}")
    public String productModifyForm(@PathVariable("id")Long id, Model model){
        authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        ModifyProductDto product = productService.getModifyProductDto(id);
        model.addAttribute("product",product);
        return "user/seller/productModify";
    }
    @PostMapping("product/modify/{id}")
    public String productModify(@PathVariable("id")Long id,
                                @ModelAttribute("product")ModifyProductDto modifyProductDto,
                                HttpServletRequest request) throws IOException {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if(userId!=null){
            productService.modifyProduct(id,modifyProductDto);
            request.setAttribute("msg", "상품 정보 수정을 완료했습니다.");
            String redirectUrl = "/product/"+id;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        return "redirect:/product/"+id;
    }
    @PostMapping("product/delete/{id}")
    public String productDelete(@PathVariable("id")Long id, HttpServletRequest request){
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if(userId!=null){
            productService.deleteProduct(userId,id);
            request.setAttribute("msg", "삭제를 완료했습니다.");
            String redirectUrl = "/";
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        else return "/";
    }

    @GetMapping("product/list")
    public String productList(@PageableDefault(page = 1) Pageable pageable, Model model,
                              @RequestParam(name = "search",required = false)String search) {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if(userId!=null){
            authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        }
        if(search==null){
            Page<PagingProductDto> allProducts = productService.getAllProducts(pageable);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), allProducts.getTotalPages());
            model.addAttribute("productDtos", allProducts);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "product/ProductList";
        }
        else {
            Page<PagingProductDto> searchProducts = productService.getProductsBySearch(pageable,search);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), searchProducts.getTotalPages());
            model.addAttribute("productDtos", searchProducts);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "product/ProductList";
        }
    }
    @GetMapping("product/list/popular")
    public String productListPopular(@PageableDefault(page = 1) Pageable pageable, Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestParam(name = "search",required = false)String search) {
        Long userId = authenticationHelper.getAuthenticatedUserId();
        if(userId!=null){
            authenticationHelper.getAuthenticatedUser().ifPresent(user -> model.addAttribute("user",user));
        }
        if(search==null){
            Page<PagingProductDto> allProducts = productService.getAllPopularProducts(pageable);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), allProducts.getTotalPages());
            model.addAttribute("productDtos", allProducts);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "product/ProductList";
        }
        else {
            Page<PagingProductDto> searchProducts = productService.getAllPopularProductsBySearch(pageable,search);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), searchProducts.getTotalPages());
            model.addAttribute("productDtos", searchProducts);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            return "product/ProductList";
        }
    }
}
