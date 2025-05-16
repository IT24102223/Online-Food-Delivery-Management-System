package model;

public class FoodItem {
    private String foodId;
    private String name;
    private String description;
    private double price;
    private String categoryId;
    private boolean availability;
    private int orderCount;

    public FoodItem(String foodId, String name, String description,
                    double price, String categoryId, boolean availability) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.availability = availability;
        this.orderCount = 0; // Default order count
    }

    public FoodItem(String foodId, String name, String description,
                    double price, String categoryId, boolean availability, int orderCount) {
        this(foodId, name, description, price, categoryId, availability);
        this.orderCount = orderCount;
    }

    // Getters & Setters
    public String getFoodId() { return foodId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategoryId() { return categoryId; }
    public boolean isAvailable() { return availability; }
    public int getOrderCount() { return orderCount; }
    public void setAvailable(boolean availability) { this.availability = availability; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%s,%b,%d",
                foodId, name, description, price, categoryId, availability, orderCount);
    }

    public static FoodItem fromCSV(String csvLine) {
        System.out.println("Parsing food item CSV: " + csvLine);
        String[] data = csvLine.split(",");
        if (data.length < 4) {
            throw new IllegalArgumentException("Invalid food item CSV format, expected at least 4 fields: " + csvLine);
        }
        try {
            String foodId = data[0];
            String name = data[1];
            double price = Double.parseDouble(data[2]);
            boolean availability = Boolean.parseBoolean(data[3]);
            // Provide defaults for optional fields
            String description = data.length > 4 && !data[4].isEmpty() ? data[4] : "No description available";
            String categoryId = data.length > 5 && !data[5].isEmpty() ? data[5] : "default";
            int orderCount = data.length > 6 && !data[6].isEmpty() ? Integer.parseInt(data[6]) : 0;
            System.out.println("Parsed food item: " + name + ", Price: " + price + ", Available: " + availability);
            return new FoodItem(foodId, name, description, price, categoryId, availability, orderCount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing food item CSV: " + e.getMessage());
        }
    }
}