package model;

import jakarta.servlet.ServletContext;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OrderQueue {
    private static final String QUEUE_FILE = "/WEB-INF/resources/data/queue.txt";
    private final Queue<Order> pendingOrders;
    private final Queue<Order> processingOrders;
    private List<Customer> customers; // For loading orders
    private List<FoodItem> foodItems; // For loading orders
    private ServletContext servletContext; // For file access

    public OrderQueue() {
        this.pendingOrders = new LinkedList<>();
        this.processingOrders = new LinkedList<>();
        this.customers = new ArrayList<>();
        this.foodItems = new ArrayList<>();
    }

    public void initialize(List<Customer> customers, List<FoodItem> foodItems, ServletContext servletContext) {
        this.customers = customers;
        this.foodItems = foodItems;
        this.servletContext = servletContext;
        loadQueue();
    }

    public synchronized void addOrder(Order order) {
        pendingOrders.add(order);
        saveQueue();
    }

    public synchronized Order processNextOrder() {
        Order order = pendingOrders.poll();
        if (order != null) {
            order.updateStatus(Order.Status.PROCESSING);
            processingOrders.add(order);
            saveQueue();
        }
        return order;
    }

    public synchronized boolean completeOrder(Order order) {
        if (processingOrders.remove(order)) {
            order.updateStatus(Order.Status.DELIVERED);
            saveQueue();
            return true;
        }
        return false;
    }

    public synchronized List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(pendingOrders);
        allOrders.addAll(processingOrders);
        return allOrders;
    }

    public synchronized int getPendingCount() {
        return pendingOrders.size();
    }

    public synchronized int getProcessingCount() {
        return processingOrders.size();
    }

    private void saveQueue() {
        if (servletContext == null) {
            System.err.println("Cannot save queue: ServletContext is null in OrderQueue");
            return;
        }
        String realPath = servletContext.getRealPath(QUEUE_FILE);
        if (realPath == null) {
            System.err.println("Cannot resolve real path for queue.txt at " + QUEUE_FILE);
            return;
        }
        System.out.println("Writing queue to: " + realPath);
        try {
            // Ensure the directory exists
            Path filePath = Paths.get(realPath);
            Files.createDirectories(filePath.getParent());
            // Create the file if it doesn't exist
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Created new queue.txt at: " + realPath);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(realPath))) {
                for (Order order : pendingOrders) {
                    String queueEntry = "PENDING," + order.getOrderId();
                    writer.write(queueEntry + "\n");
                    System.out.println("Wrote to queue.txt: " + queueEntry);
                }
                for (Order order : processingOrders) {
                    String queueEntry = "PROCESSING," + order.getOrderId();
                    writer.write(queueEntry + "\n");
                    System.out.println("Wrote to queue.txt: " + queueEntry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving queue to " + realPath + ": " + e.getMessage());
        }
    }

    private void loadQueue() {
        pendingOrders.clear();
        processingOrders.clear();
        if (servletContext == null) {
            System.err.println("Cannot load queue: ServletContext is null in OrderQueue");
            return;
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(servletContext.getResourceAsStream(QUEUE_FILE)))) {
            if (reader == null) {
                System.err.println("Cannot find queue.txt at " + QUEUE_FILE);
                return;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 2) continue;
                String queueType = data[0];
                String orderId = data[1];
                try (BufferedReader orderReader = new BufferedReader(
                        new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/resources/data/orders.txt")))) {
                    String orderLine;
                    while ((orderLine = orderReader.readLine()) != null) {
                        try {
                            Order order = Order.fromCSV(orderLine, customers, foodItems);
                            if (order.getOrderId().equals(orderId)) {
                                if ("PENDING".equals(queueType)) {
                                    pendingOrders.add(order);
                                } else if ("PROCESSING".equals(queueType)) {
                                    processingOrders.add(order);
                                }
                                break;
                            }
                        } catch (IllegalArgumentException e) {
                            System.err.println("Skipping order in queue due to error: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading orders.txt for queue: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading queue: " + e.getMessage());
        }
    }
}