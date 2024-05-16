package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.domain.entity.OrderItem;
import com.tanguri.shopping.repository.OrderItemRepository;
import com.tanguri.shopping.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    public List<Order> getOrderItemsByUserId(Long id){
        return orderRepository.findAllByUserId(id);
    }
    public Integer getTotalOrderCount(Long id){
        List<Order> orders = orderRepository.findAllByUserId(id);
        Integer totalOrderCount=0;
        for (Order order : orders) {
            totalOrderCount+=order.getProductCount();
        }
        return totalOrderCount;
    }
}
