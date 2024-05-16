package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
}
