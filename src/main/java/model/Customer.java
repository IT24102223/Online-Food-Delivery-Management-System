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
import java.util.List;

public class Customer extends User {
    private static final String ORDERS_FILE = "/WEB-INF/resources/data/orders.txt";
    private static final String MENU_FILE = "/WEB-INF/resources/data/menu.txt";
    private final Cart cart;
    private final List<Order> orderHistory;
    private final OrderQueue orderQueue;
    private final ServletContext servletContext;

    public Customer(String username, String password, String email, String phone, String address,
                    OrderQueue orderQueue, ServletContext servletContext) {
        super(username, password, email, phone, address, "CUSTOMER");
        this.cart = new Cart();
        this.orderHistory = new ArrayList<>();
        this.orderQueue = orderQueue;
        this.servletContext = servletContext;
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        orderHistory.clear();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(servletContext.getResourceAsStream(ORDERS_FILE)))) {
            if (reader == null) {
                System.err.println("Cannot find orders.txt at " + ORDERS_FILE);
                return;
            }
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Order order = Order.fromCSV(line, List.of(this), getAllFoodItems());
                    if (order.getStatus() == null) {
                        System.err.println("Skipping order at line " + lineNumber + ": Null status");
                        continue;
                    }
                    System.out.println("Parsing order at line " + lineNumber + ": " + line);
                    System.out.println("Order customer ID: " + order.getCustomer().getUserId() +
                            ", Current user ID: " + this.getUserId());
                    if (order.getCustomer().getUserId() != null &&
                            order.getCustomer().getUserId().equals(this.getUserId())) {
                        orderHistory.add(order);
                        System.out.println("Loaded order: " + order.getOrderId() + " for user " + getUserId());
                    } else {
                        System.out.println("Skipping order: Customer ID mismatch. Order Customer ID: " +
                                order.getCustomer().getUserId() + ", Current User ID: " + this.getUserId());
                    }
                } catch (Exception e) {
                    System.err.println("Skipping invalid order CSV at line " + lineNumber + ": " + e.getMessage());
                }
            }
            System.out.println("Loaded " + orderHistory.size() + " orders for user " + getUserId());
        } catch (IOException e) {
            System.err.println("Error reading orders.txt: " + e.getMessage());
        }
    }

    public void placeOrder() {
        if (cart.isEmpty()) throw new IllegalStateException("Cart is empty");
        Order order = new Order(this, cart.getItems());
        System.out.println("Placing order: " + order.getOrderId() + " for user: " + getUserId());
        orderQueue.addOrder(order);

        // Load existing orders, add the new one, and rewrite the file
        List<Order> allOrders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(servletContext.getResourceAsStream(ORDERS_FILE)))) {
            if (reader != null) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        Order existingOrder = Order.fromCSV(line, List.of(this), getAllFoodItems());
                        allOrders.add(existingOrder);
                    } catch (Exception e) {
                        System.err.println("Skipping invalid order while loading for rewrite: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading orders.txt for rewrite: " + e.getMessage());
        }

        allOrders.add(order);

        String realPath = servletContext.getRealPath(ORDERS_FILE);
        if (realPath == null) {
            System.err.println("Cannot resolve real path for orders.txt at " + ORDERS_FILE);
            throw new RuntimeException("Cannot resolve path for orders.txt");
        }
        System.out.println("Writing orders to: " + realPath);
        try {
            Path filePath = Paths.get(realPath);
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Created new orders.txt at: " + realPath);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(realPath, false))) {
                for (Order o : allOrders) {
                    String orderCsv = o.toCSV();
                    writer.write(orderCsv + "\n");
                    System.out.println("Wrote order to orders.txt: " + orderCsv);
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving orders to " + realPath + ": " + e.getMessage());
            throw new RuntimeException("Failed to save orders", e);
        }

        cart.save(servletContext);
        loadOrderHistory();
        System.out.println("Order history size after placing order: " + orderHistory.size());
        cart.clear();
    }

    public Cart getCart() { return cart; }
    public List<Order> getOrderHistory() {
        loadOrderHistory();
        return new ArrayList<>(orderHistory);
    }

    private List<FoodItem> getAllFoodItems() {
        List<FoodItem> foodItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(servletContext.getResourceAsStream(MENU_FILE)))) {
            if (reader == null) {
                System.err.println("Cannot find menu.txt at " + MENU_FILE);
                return foodItems;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    foodItems.add(FoodItem.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing food item CSV: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading food items: " + e.getMessage());
            throw new RuntimeException("Failed to load food items", e);
        }
        return foodItems;
    }

    @Override
    public String toCSV() {
        return String.join(",", getUserId(), getUsername(), getPassword(), getEmail(),
                getPhone(), getAddress(), "CUSTOMER", String.valueOf(isActive()));
    }

    public static Customer fromCSV(String csvLine, OrderQueue orderQueue, ServletContext servletContext) {
        String[] data = csvLine.split(",");
        if (data.length < 8) {
            throw new IllegalArgumentException("Invalid CSV format for Customer");
        }
        Customer customer = new Customer(data[1], data[2], data[3], data[4], data[5],
                orderQueue, servletContext);
        try {
            java.lang.reflect.Field userIdField = User.class.getDeclaredField("userId");
            userIdField.setAccessible(true);
            userIdField.set(customer, data[0]);
            System.out.println("Set customer userId: " + data[0] + " for username: " + data[1]);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting Customer userId: " + e.getMessage());
        }
        customer.setActive(Boolean.parseBoolean(data[7]));
        customer.orderQueue.initialize(List.of(customer), customer.getAllFoodItems(), servletContext);
        return customer;
    }
}