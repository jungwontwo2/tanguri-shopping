package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.Delivery;
import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.repository.DeliveryRepository;
import com.tanguri.shopping.repository.OrderRepository;
import com.tanguri.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public void startDelivery(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderItemId(orderId);
        delivery.startDelivery();
        deliveryRepository.save(delivery);
        Order order = orderRepository.findById(orderId).orElse(null);
        order.startDelivery();
        orderRepository.save(order);
    }
}
