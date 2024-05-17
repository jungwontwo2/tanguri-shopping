package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.CartItem;
import com.tanguri.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @PostMapping("user/cart/{cartItemId}/delete")
    public String deleteCartItemInCart(@PathVariable("cartItemId")Long cartItemId,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Long id = customUserDetails.getId();
        cartService.deleteProductInCart(cartItemId);
        return "redirect:/user/cart/"+id;
    }
    @PostMapping("/user/cart/checkout/{id}")
    public String buyCartItems(@PathVariable("id")Long id,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        cartService.orderCartItems(id);
        List<CartItem> cartItems = cartService.getCartItemsByCartId(customUserDetails.getUserEntity().getCart().getId());
        for (CartItem cartItem : cartItems) {
            cartService.deleteProductInCart(cartItem.getId());
        }
        return "redirect:/user/cart/"+id;
    }
}
