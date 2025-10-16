package com.services.order_service.controller;

import com.services.order_service.client.UserClient;
import com.services.order_service.dto.OrderResponse;
import com.services.order_service.entity.Order;
import com.services.order_service.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    @Autowired
    private UserClient userClient;

    private Map<Long, Order> orders = new HashMap<>();

    public OrderController() {
        orders.put(1L, new Order(1L, 1L, "Laptop", 1200.00));
        orders.put(2L, new Order(2L, 2L, "Mouse", 25.00));
        orders.put(3L, new Order(3L, 1L, "Teclado", 80.00));
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        System.out.println("Buscando orden: " + id);
        Order order = orders.get(id);

        if (order == null) {
            throw new RuntimeException("Orden no encontrada");
        }

        // Llamada a user-service usando Feign
        System.out.println("Llamando a user-service para usuario: " + order.getUserId());
        User user = userClient.getUserById(order.getUserId());

        return new OrderResponse(order, user);
    }

    @GetMapping
    public Collection<Order> getAllOrders() {
        return orders.values();
    }
}