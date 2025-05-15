package model;

import java.io.*;
import java.util.*;

public class OrderQueue {
    private static final String FILE_NAME = "orders.txt";

    // Add order
    public void addOrder(Order order) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
        writer.write(order.toFileString());
        writer.newLine();
        writer.close();
    }

    // Get all orders
    public List<Order> getAllOrders() throws IOException {
        List<Order> orders = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return orders;

        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            orders.add(Order.fromFileString(line));
        }
        reader.close();
        return orders;
    }

    // Update order status
    public void updateOrderStatus(String orderId, String newStatus) throws IOException {
        List<Order> orders = getAllOrders();
        for (Order o : orders) {
            if (o.getId().equals(orderId)) {
                o.setStatus(newStatus);
                break;
            }
        }
        saveAllOrders(orders);
    }

    // Delete order by ID
    public void deleteOrder(String orderId) throws IOException {
        List<Order> orders = getAllOrders();
        orders.removeIf(o -> o.getId().equals(orderId));
        saveAllOrders(orders);
    }

    // Save list of orders to file
    private void saveAllOrders(List<Order> orders) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (Order o : orders) {
            writer.write(o.toFileString());
            writer.newLine();
        }
        writer.close();
    }
}

