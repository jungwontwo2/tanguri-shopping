package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.product.*;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.CartItem;
import com.tanguri.shopping.domain.entity.Product;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.service.OrderService;
import com.tanguri.shopping.service.ProductService;
import com.tanguri.shopping.service.UserService;
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

    @GetMapping("product/upload")
    public String productUploadForm(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    Model model, @ModelAttribute("product") AddProductDto addProductDto) {
        Long id = customUserDetails.getUserEntity().getId();
        model.addAttribute("user", userService.findUser(id));
        return "user/seller/addProduct";
    }

    @PostMapping("product/upload")
    public String productUpload(@Validated @ModelAttribute("product") AddProductDto addProductDto, BindingResult bindingResult,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model,
                                HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            Long id = customUserDetails.getUserEntity().getId();
            model.addAttribute("user", userService.findUser(id));
            return "user/seller/addProduct";
        }
        Long productId = productService.uploadProduct(addProductDto, customUserDetails.getId());
        request.setAttribute("msg", "상품 업로드를 완료했습니다.");
        String redirectUrl = "/product/" + productId;
        request.setAttribute("redirectUrl", redirectUrl);
        return "common/messageRedirect";
    }

    @GetMapping("product/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @ModelAttribute("buyOrCartProductDto") BuyOrCartProductDto buyOrCartProductDto) {
        if (customUserDetails != null) {
            Long userId = customUserDetails.getUserEntity().getId();
            Cart cart = userService.getCartByLoginId(userId);
            List<CartItem> cartItems = cart.getCartItems();
            int totalProductCount = 0;
            for (CartItem cartItem : cartItems) {
                totalProductCount += cartItem.getCount();
            }
            model.addAttribute("totalProductCount", totalProductCount);
            model.addAttribute("user", userService.findUser(userId));
        }
        ViewProductDto product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "product/ProductView";
    }

    @PostMapping("product/order/{productId}")
    public String buyProduct(@PathVariable("productId") Long productId,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                             HttpServletRequest request) {
        Long id = customUserDetails.getUserEntity().getId();
        Integer money = userService.getMoney(id);
        if (productService.getProduct(productId).getPrice() * buyOrCartProductDto.getCount() > money) {
            request.setAttribute("msg", "잔액이 부족합니다.\n 충전 후 다시 사용바랍니다.");
            String redirectUrl = "/product/" + productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        } else if (productService.getProduct(productId).getStock() < buyOrCartProductDto.getCount()) {
            request.setAttribute("msg", "재고가 부족합니다.\n최대 수량은" + productService.getProduct(productId).getStock() + "개 입니다");
            String redirectUrl = "/product/" + productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        } else {
            orderService.orderProduct(productId, customUserDetails.getId(), buyOrCartProductDto);
            request.setAttribute("msg", "주문을 완료했습니다.");
            String redirectUrl = "/product/"+productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
    }

    @PostMapping("product/cart/{productId}")
    public String productInCart(@PathVariable("productId") Long productId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                                HttpServletRequest request) {
        Long id = customUserDetails.getUserEntity().getId();
        productService.productInCart(productId, customUserDetails.getId(), buyOrCartProductDto);
        request.setAttribute("msg", "장바구니에 상품을 담았습니다.");
        String redirectUrl = "/product/"+productId;
        request.setAttribute("redirectUrl", redirectUrl);
        return "common/messageRedirect";
    }

    @GetMapping("product/modify/{id}")
    public String productModifyForm(@PathVariable("id")Long id, Model model,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails){
        User user = userService.findUser(customUserDetails.getId());
        model.addAttribute("user",user);
        ModifyProductDto product = productService.getModifyProductDto(id);
        model.addAttribute("product",product);
        return "user/seller/productModify";
    }
    @PostMapping("product/modify/{id}")
    public String productModify(@PathVariable("id")Long id,
                                @ModelAttribute("product")ModifyProductDto modifyProductDto,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                HttpServletRequest request) throws IOException {
        if (customUserDetails!=null){
            productService.modifyProduct(id,modifyProductDto);
            request.setAttribute("msg", "상품 정보 수정을 완료했습니다.");
            String redirectUrl = "/product/"+id;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        return "redirect:/product/"+id;
    }
    @PostMapping("product/delete/{id}")
    public String productDelete(@PathVariable("id")Long id,@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                HttpServletRequest request){
        if(customUserDetails!=null){
            productService.deleteProduct(customUserDetails.getId(),id);
            request.setAttribute("msg", "삭제를 완료했습니다.");
            String redirectUrl = "/";
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        else return "/";
    }

    @GetMapping("product/list")
    public String productList(@PageableDefault(page = 1) Pageable pageable, Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @RequestParam(name = "search",required = false)String search) {
        if (customUserDetails != null) {
            Long id = customUserDetails.getUserEntity().getId();
            model.addAttribute("user", userService.findUser(id));
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
        if (customUserDetails != null) {
            Long id = customUserDetails.getUserEntity().getId();
            model.addAttribute("user", userService.findUser(id));
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
