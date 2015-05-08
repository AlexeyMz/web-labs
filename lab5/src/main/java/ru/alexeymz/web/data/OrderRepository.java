package ru.alexeymz.web.data;

import ru.alexeymz.web.model.Order;

import java.util.List;

public interface OrderRepository {
    public static final String ATTRIBUTE = "ORDER_REPOSITORY";

    List<Order> findByUsername(String username);
    void save(Order order);
}
