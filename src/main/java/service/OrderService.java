package service;

import model.*;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private static final String ORDERS_FILE = "/WEB-INF/resources/data/orders.txt";
    private static final String MENU_FILE = "/WEB-INF/resources/data/menu.txt";

    public List<Order> getAllOrders(List<Customer> customers, List<FoodItem> foodItems, ServletContext context) {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResourceAsStream(ORDERS_FILE)))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    orders.add(Order.fromCSV(line, customers, foodItems));
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid order CSV at line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading orders from " + ORDERS_FILE + ": " + e.getMessage());
        }
        return orders;
    }

    public Order getOrderById(String orderId, Customer customer, ServletContext context) {
        List<FoodItem> foodItems = loadFoodItems(context);
        List<Customer> customers = customer != null ? List.of(customer) : new ArrayList<>();
        List<Order> orders = getAllOrders(customers, foodItems, context);
        return orders.stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public void updateOrder(Order order, ServletContext context) {
        List<FoodItem> foodItems = loadFoodItems(context);
        List<Order> orders = getAllOrders(new ArrayList<>(), foodItems, context);
        orders.removeIf(o -> o.getOrderId().equals(order.getOrderId()));
        orders.add(order);
        saveOrders(orders, context);
    }

    public void cancelOrder(String orderId, String userId, ServletContext context) {
        Order order = getOrderById(orderId, null, context);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if (!order.getCustomer().getUserId().equals(userId)) {
            throw new SecurityException("Not authorized to cancel this order");
        }
        order.cancel();
        updateOrder(order, context);
    }

    public void updateOrderStatus(String orderId, String userId, Order.Status status, ServletContext context) {
        Order order = getOrderById(orderId, null, context);
        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if (!order.getCustomer().getUserId().equals(userId)) {
            throw new SecurityException("Not authorized to update this order");
        }
        order.updateStatus(status);
        updateOrder(order, context);
    }

    private void saveOrders(List<Order> orders, ServletContext context) {
        try {
            String realPath = context.getRealPath(ORDERS_FILE);
            if (realPath == null) {
                System.err.println("Cannot resolve real path for orders.txt");
                throw new RuntimeException("Cannot resolve path for orders.txt");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(realPath))) {
                for (Order order : orders) {
                    writer.write(order.toCSV() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving orders to " + ORDERS_FILE + ": " + e.getMessage());
            throw new RuntimeException("Failed to save orders", e);
        }
    }

    private List<FoodItem> loadFoodItems(ServletContext context) {
        List<FoodItem> foodItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResourceAsStream(MENU_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    foodItems.add(FoodItem.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing food item CSV: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading food items from " + MENU_FILE + ": " + e.getMessage());
        }
        return foodItems;
    }
}