package com.tanguri.shopping.service;

import com.tanguri.shopping.domain.entity.Delivery;
import com.tanguri.shopping.domain.entity.Order;
import com.tanguri.shopping.domain.entity.User;
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

    public void completeDelivery(Long orderId,Long userId) {
        Delivery delivery = deliveryRepository.findByOrderItemId(orderId);
        delivery.completeDelivery();
        deliveryRepository.save(delivery);
        Order order = orderRepository.findById(orderId).orElse(null);
        order.completeDelivery();
        orderRepository.save(order);
        User user = userRepository.findById(userId).orElse(null);
        user.addEarning(orderRepository.findById(orderId).get().getTotalPrice());
        userRepository.save(user);
    }

    public void cancelDelivery(Long orderId) {
        Delivery delivery = deliveryRepository.findByOrderItemId(orderId);
        delivery.cancelDelivery();
        deliveryRepository.save(delivery);
        Order order = orderRepository.findById(orderId).orElse(null);
        order.cancelOrder();
        orderRepository.save(order);
        Long userId = order.getUser().getId();
        User user = userRepository.findByUserId(userId).orElse(null);
        user.addMoney(order.getTotalPrice());
        userRepository.save(user);
    }
}
