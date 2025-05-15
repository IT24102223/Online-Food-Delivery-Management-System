package model;

public class Order {
    private String id;
    private String customerName;
    private String foodItem;
    private String status; // Pending, In Progress, Completed

    public Order(String id, String customerName, String foodItem, String status) {
        this.id = id;
        this.customerName = customerName;
        this.foodItem = foodItem;
        this.status = status;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getFoodItem() { return foodItem; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    // Convert object to CSV string for file
    public String toFileString() {
        return id + "," + customerName + "," + foodItem + "," + status;
    }

    // Create object from file line
    public static Order fromFileString(String line) {
        String[] parts = line.split(",");
        return new Order(parts[0], parts[1], parts[2], parts[3]);
    }
}

