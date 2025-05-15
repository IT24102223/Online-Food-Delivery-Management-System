package service;

import model.*;
import jakarta.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

public class OrderQueueService {
    private final OrderQueue orderQueue;

    public OrderQueueService() {
        this.orderQueue = new OrderQueue();
    }

    public void initialize(List<Customer> customers, List<FoodItem> foodItems, ServletContext servletContext) {
        orderQueue.initialize(customers, foodItems, servletContext);
    }

    public void addOrder(Order order) {
        orderQueue.addOrder(order);
    }

    public Order processNextOrder() {
        return orderQueue.processNextOrder();
    }

    public boolean completeOrder(Order order) {
        return orderQueue.completeOrder(order);
    }

    public List<Order> getAllOrders() {
        return orderQueue.getAllOrders();
    }
}