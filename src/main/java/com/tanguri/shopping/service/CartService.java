package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.product.BuyOrCartProductDto;
import com.tanguri.shopping.domain.entity.Cart;
import com.tanguri.shopping.domain.entity.CartItem;
import com.tanguri.shopping.domain.entity.User;
import com.tanguri.shopping.repository.CartItemRepository;
import com.tanguri.shopping.repository.CartRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public List<CartItem> getCartItemsByCartId(Long cartId){
        return cartItemRepository.findAllByCartId(cartId);
    }
    public void deleteProductInCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        cartItemRepository.delete(cartItem);
    }
    public Integer getTotalProductCount(Long id){
        User user = userRepository.findById(id).orElse(null);
        List<CartItem> cartItems = user.getCart().getCartItems();
        Integer totalProductCount=0;
        for (CartItem cartItem : cartItems) {
            totalProductCount+=cartItem.getCount();
        }
        return totalProductCount;
    }
    public void orderCartItems(Long id){
        User user = userRepository.findByUserId(id).orElse(null);
        List<CartItem> cartItems = user.getCart().getCartItems();
        for (CartItem cartItem : cartItems) {
            orderService.orderProduct(cartItem.getProduct().getId(),id,new BuyOrCartProductDto(cartItem.getCount()));
        }
    }
}
