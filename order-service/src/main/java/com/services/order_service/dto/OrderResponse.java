package com.services.order_service.dto;

import com.services.order_service.entity.Order;
import com.services.order_service.entity.User;

public class OrderResponse {

    private Order order;
    private User user;

    public OrderResponse() {}

    public OrderResponse(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
