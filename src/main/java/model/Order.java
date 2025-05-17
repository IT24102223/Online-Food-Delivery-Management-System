package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

public class Order {
    private final String orderId;
    private final Customer customer;
    private final List<OrderItem> items;
    private final double total;
    private Status status;
    private final LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public enum Status {
        PENDING, PROCESSING, DELIVERED, CANCELLED
    }

    public Order(Customer customer, List<OrderItem> items) {
        this.orderId = UUID.randomUUID().toString();
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.total = calculateTotal();
        this.status = Status.PENDING;
        this.orderDate = LocalDateTime.now();
        this.deliveryDate = null;
    }

    private Order(String orderId, Customer customer, List<OrderItem> items, double total,
                  Status status, LocalDateTime orderDate, LocalDateTime deliveryDate) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.total = total;
        this.status = status != null ? status : Status.PENDING;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    private double calculateTotal() {
        return items.stream()
                .mapToDouble(item -> item.getFoodItem().getPrice() * item.getQuantity())
                .sum();
    }

    public void updateStatus(Status newStatus) {
        if (status == Status.DELIVERED || status == Status.CANCELLED) {
            throw new IllegalStateException("Cannot change status from " + status);
        }
        if (newStatus == Status.PENDING) {
            throw new IllegalStateException("Cannot revert to PENDING");
        }
        this.status = newStatus;
        if (newStatus == Status.DELIVERED) {
            this.deliveryDate = LocalDateTime.now();
        }
    }

    public void cancel() {
        if (status == Status.DELIVERED || status == Status.CANCELLED) {
            throw new IllegalStateException("Cannot cancel order in " + status + " status");
        }
        this.status = Status.CANCELLED;
    }

    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public double getTotal() { return total; }
    public Status getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getDeliveryDate() { return deliveryDate; }

    public String toCSV() {
        String itemsStr = items.stream()
                .map(item -> item.getFoodItem().getFoodId() + ":" + item.getQuantity())
                .collect(Collectors.joining(";"));
        String deliveryDateStr = deliveryDate != null ? deliveryDate.format(formatter) : "";
        return String.join(",", orderId, customer.getUserId(), itemsStr,
                String.format("%.2f", total), status.toString(), orderDate.format(formatter), deliveryDateStr);
    }

    public static Order fromCSV(String csv, List<Customer> customers, List<FoodItem> foodItems) {
        String[] data = csv.split(",", -1);
        if (data.length < 6) {
            throw new IllegalArgumentException("Invalid order CSV format: " + csv);
        }
        String orderId = data[0];
        String customerId = data[1];
        String itemsStr = data[2];
        double total;
        try {
            total = Double.parseDouble(data[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid total in order CSV: " + csv);
        }
        Status status;
        try {
            status = Status.valueOf(data[4].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status in order CSV: " + data[4] + ", defaulting to PENDING");
            status = Status.PENDING;
        }
        LocalDateTime orderDate;
        try {
            orderDate = LocalDateTime.parse(data[5], formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid order date in order CSV: " + csv);
        }
        LocalDateTime deliveryDate = null;
        if (data.length > 6 && !data[6].isEmpty()) {
            try {
                deliveryDate = LocalDateTime.parse(data[6], formatter);
            } catch (Exception e) {
                System.err.println("Invalid delivery date in order CSV: " + data[6] + ", setting to null");
            }
        }

        Customer customer = customers.stream()
                .filter(c -> customerId.equals(c.getUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found for ID: " + customerId + " in provided list"));

        List<OrderItem> items = new ArrayList<>();
        for (String itemEntry : itemsStr.split(";")) {
            String[] itemData = itemEntry.split(":");
            if (itemData.length != 2) continue;
            String foodId = itemData[0];
            int quantity;
            try {
                quantity = Integer.parseInt(itemData[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity in items: " + itemEntry);
                continue;
            }
            FoodItem foodItem = foodItems.stream()
                    .filter(f -> foodId.equals(f.getFoodId()))
                    .findFirst()
                    .orElse(null);
            if (foodItem != null) {
                items.add(new OrderItem(foodItem, quantity));
            }
        }

        return new Order(orderId, customer, items, total, status, orderDate, deliveryDate);
    }
}