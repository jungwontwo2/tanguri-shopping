package com.tanguri.shopping.controller;

import com.tanguri.shopping.domain.dto.user.CustomUserDetails;
import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.service.OrderService;
import com.tanguri.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    //주문내역
    @GetMapping("/orderHist/{id}")
    public String orderHistory(@PathVariable("id")Long id, @AuthenticationPrincipal CustomUserDetails customUserDetails,
                               Model model){
        User user = userService.findUser(customUserDetails.getId());
        List<Order> orders = orderService.getOrderItemsByUserId(customUserDetails.getId());
        Integer totalOrderCount = orderService.getTotalOrderCount(customUserDetails.getId());
        model.addAttribute("orders",orders);
        model.addAttribute("totalOrderCount",totalOrderCount);
        model.addAttribute("user",user);
        return "user/buyer/userOrderList";
    }
    @PostMapping("/{id}/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("id")Long userId,@PathVariable("orderId")Long orderId,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        orderService.cancelOrder(userId,orderId);
        return "redirect:/orderHist/"+userId;
    }
}
