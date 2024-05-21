package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.dto.error.CustomException;
import com.tanguri.shopping.domain.dto.error.ErrorCode;
import com.tanguri.shopping.domain.dto.product.BuyOrCartProductDto;
import com.tanguri.shopping.domain.entity.*;
import com.tanguri.shopping.domain.enums.Status;
import com.tanguri.shopping.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    public Long orderProduct(Long productId, Long userId, BuyOrCartProductDto buyOrCartProductDto){
        Product product = productRepository.findById(productId).orElse(null);
        if(product.getStock()<buyOrCartProductDto.getCount()){
            throw new CustomException(ErrorCode.STOCK_LACK);
        }
        product.decreaseStock(buyOrCartProductDto.getCount());
        product.addSoldProductCount(buyOrCartProductDto.getCount());
        User user = userRepository.findById(userId).orElse(null);
        if(buyOrCartProductDto.getCount()*product.getPrice()>user.getMoney()){
            throw new CustomException(ErrorCode.MONEY_LACK);
        }
        user.useMoney(buyOrCartProductDto.getCount()*product.getPrice());
        Order order = Order.builder()
                .user(user)
                .product(product)
                .productCount(buyOrCartProductDto.getCount())
                .totalPrice(buyOrCartProductDto.getCount()*product.getPrice())
                .status(Status.배송준비)
                .orderDate(LocalDateTime.now())
                .build();
        Delivery delivery = Delivery.builder()
                .order(order)
                .address(user.getAddress())
                .addressNumber(user.getAddressNumber())
                .detailAddress(user.getDetailAddress())
                .status(Status.배송준비)
                .build();
        user.getOrders().add(order);
        orderRepository.save(order);
        deliveryRepository.save(delivery);
        return order.getId();
    }

    public List<Order> getOrderItemsByUserId(Long id){
        return orderRepository.findAllByUserId(id);
    }
    public Integer getTotalOrderCount(Long id){
        List<Order> orders = orderRepository.findAllByUserId(id);
        Integer totalOrderCount=0;
        for (Order order : orders) {
            if(order.getStatus()!=Status.주문취소){
                totalOrderCount+=order.getProductCount();
            }
        }
        return totalOrderCount;
    }
    public List<Order> getSellerItems(Long id){
        return orderRepository.findOrderItemByProductId(id);
    }

    public Order findByOrderId(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void cancelOrder(Long userId,Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        order.cancelOrder();
        orderRepository.save(order);
        Product product = order.getProduct();
        product.minusProductCount(order.getProductCount());
        User user = userRepository.findByUserId(userId).orElse(null);
        user.addMoney(order.getTotalPrice());
        userRepository.save(user);
    }
}
