package service;

import model.Order;
import model.FoodItem;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {
    private static final String FILE_PATH = "WEB-INF/resources/data/orders.txt";
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final ServletContext servletContext;
    private final FoodItemService foodItemService;

    public OrderService(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.foodItemService = new FoodItemService(servletContext);
    }

    public void placeOrder(Order order) throws IOException {
        List<Order> orders = getAllOrders();
        orders.add(order);
        try {
            saveAllOrders(orders);
            LOGGER.info("Successfully placed order " + order.getOrderId() + " in orders.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save order " + order.getOrderId() + " to orders.txt", e);
            throw e;
        }
    }

    public List<Order> getAllOrders() throws IOException {
        List<Order> orders = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        LOGGER.info("Reading orders.txt from: " + realPath);
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("orders.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Order order = Order.fromCSV(line);
                List<FoodItem> items = new ArrayList<>();
                if (data.length > 2 && !data[2].isEmpty()) {
                    String[] itemIds = data[2].split(";");
                    for (String itemId : itemIds) {
                        if (!itemId.isEmpty()) {
                            FoodItem item = foodItemService.getFoodItemById(itemId);
                            if (item != null) {
                                items.add(item);
                            } else {
                                LOGGER.warning("Food item with ID " + itemId + " not found for order " + order.getOrderId());
                            }
                        }
                    }
                }
                order.setItems(items);
                orders.add(order);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading orders.txt", e);
            throw e;
        }
        return orders;
    }

    private void saveAllOrders(List<Order> orders) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        LOGGER.info("Writing to orders.txt at: " + realPath);
        File file = new File(realPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            LOGGER.info("Created parent directories for " + realPath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Order order : orders) { // Fixed: Changed 'allOrders' to 'orders'
                writer.write(order.toCSV());
                writer.newLine();
            }
            LOGGER.info("Successfully wrote " + orders.size() + " orders to orders.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to orders.txt", e);
            throw e;
        }
    }

    public void updateOrderStatus(String orderId, String newStatus) throws IOException {
        List<Order> orders = getAllOrders();
        boolean updated = false;
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(orderId)) {
                orders.get(i).setStatus(newStatus);
                updated = true;
                break;
            }
        }
        if (!updated) {
            LOGGER.warning("No order found with ID " + orderId + " for status update");
            throw new IOException("Order with ID " + orderId + " not found");
        }
        saveAllOrders(orders);
        LOGGER.info("Successfully updated status of order " + orderId + " to " + newStatus + " in orders.txt");
    }

    public void cancelOrder(String orderId) throws IOException {
        updateOrderStatus(orderId, "Cancelled");
    }

    public Order getOrderById(String orderId) throws IOException {
        return getAllOrders().stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public List<Order> getConfirmedOrders() throws IOException {
        return getAllOrders().stream()
                .filter(order -> "Confirmed".equals(order.getStatus()))
                .toList();
    }

    public void deleteOrder(String orderId) throws IOException {
        List<Order> orders = getAllOrders();
        boolean deleted = orders.removeIf(order -> order.getOrderId().equals(orderId));
        if (!deleted) {
            LOGGER.warning("No order found with ID " + orderId + " for deletion");
            throw new IOException("Order with ID " + orderId + " not found");
        }
        saveAllOrders(orders);
        LOGGER.info("Successfully deleted order " + orderId + " from orders.txt");
    }
}