package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.service.DeliveryService;
import com.tanguri.shopping.service.ProductService;
import com.tanguri.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SellerController {
    private final UserService userService;
    private final ProductService productService;
    private final DeliveryService deliveryService;

    @PostMapping("/seller/delivery/start/{orderId}")
    public String startDelivery(@PathVariable("orderId") Long orderId,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getUserEntity().getId();
        deliveryService.startDelivery(orderId);
        return "redirect:/seller/sellHist/"+id;
    }
}
