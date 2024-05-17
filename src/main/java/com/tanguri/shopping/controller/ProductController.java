package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.product.AddProductDto;
import com.tanguri.shopping.domain.dto.product.BuyOrCartProductDto;
import com.tanguri.shopping.domain.dto.product.ViewProductDto;
import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.CartItem;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.service.OrderService;
import com.tanguri.shopping.service.ProductService;
import com.tanguri.shopping.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
                                    Model model, @ModelAttribute("product")AddProductDto addProductDto){
        Long id = customUserDetails.getUserEntity().getId();
        model.addAttribute("user", userService.findUser(id));
        return "user/seller/addProduct";
    }
    @PostMapping("product/upload")
    public String productUpload(@Validated @ModelAttribute("product") AddProductDto addProductDto, BindingResult bindingResult,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,Model model) throws IOException {
        if(bindingResult.hasErrors()){
            Long id = customUserDetails.getUserEntity().getId();
            model.addAttribute("user", userService.findUser(id));
            return "user/seller/addProduct";
        }
        productService.uploadProduct(addProductDto,customUserDetails.getId());
        return "redirect:/";
    }

    @GetMapping("product/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails,
                              @ModelAttribute("buyOrCartProductDto")BuyOrCartProductDto buyOrCartProductDto){
        if(customUserDetails!=null){
            Long userId = customUserDetails.getUserEntity().getId();
            Cart cart = userService.getCartByLoginId(userId);
            List<CartItem> cartItems = cart.getCartItems();
            int totalProductCount = 0;
            for (CartItem cartItem : cartItems) {
                totalProductCount += cartItem.getCount();
            }
            model.addAttribute("totalProductCount",totalProductCount);
            model.addAttribute("user", userService.findUser(userId));
        }
        ViewProductDto product = productService.getProduct(id);
        model.addAttribute("product",product);
        return "product/ProductView";
    }
    @PostMapping("product/order/{productId}")
    public String buyProduct(@PathVariable("productId")Long productId,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                             HttpServletRequest request){
        Long id = customUserDetails.getUserEntity().getId();
        Integer money = userService.getMoney(id);
        if(productService.getProduct(productId).getPrice()*buyOrCartProductDto.getCount()>money){
            request.setAttribute("msg","잔액이 부족합니다.\n 충전 후 다시 사용바랍니다.");
            String redirectUrl="/product/"+productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        else if(productService.getProduct(productId).getStock()<buyOrCartProductDto.getCount()){
            request.setAttribute("msg","재고가 부족합니다.\n최대 수량은"+productService.getProduct(productId).getStock()+"개 입니다");
            String redirectUrl="/product/"+productId;
            request.setAttribute("redirectUrl", redirectUrl);
            return "common/messageRedirect";
        }
        else {
            orderService.orderProduct(productId,customUserDetails.getId(),buyOrCartProductDto);
            System.out.println("buyOrCartProductDto = " + buyOrCartProductDto.getCount());
            return "redirect:/product/"+productId;
        }
    }
    @PostMapping("product/cart/{productId}")
    public String ProductInCart(@PathVariable("productId")Long productId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @ModelAttribute("product") BuyOrCartProductDto buyOrCartProductDto,
                                HttpServletRequest request){
        Long id = customUserDetails.getUserEntity().getId();
        //Integer money = userService.getMoney(id);
//        if(productService.getProduct(productId).getPrice()*buyOrCartProductDto.getCount()>money){
//            request.setAttribute("msg","잔액이 부족합니다.\n 충전 후 다시 사용바랍니다.");
//            String redirectUrl="/product/"+productId;
//            request.setAttribute("redirectUrl", redirectUrl);
//            return "common/messageRedirect";
//        }
//        else if(productService.getProduct(productId).getStock()<buyOrCartProductDto.getCount()){
//            request.setAttribute("msg","재고가 부족합니다.\n최대 수량은"+productService.getProduct(productId).getStock()+"개 입니다");
//            String redirectUrl="/product/"+productId;
//            request.setAttribute("redirectUrl", redirectUrl);
//            return "common/messageRedirect";
//        }
        //else {
            productService.productInCart(productId,customUserDetails.getId(),buyOrCartProductDto);
            return "redirect:/product/"+productId;
        //}
    }
}
