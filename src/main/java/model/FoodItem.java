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

    // Constructor with orderCount
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
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public void addFoodItem() {
        // Logic to add food item, Implemented in FoodItemServlet
    }

    public void updateFoodItem() {
        // Logic to update food item, Implemented in FoodItemServlet
    }

    public  void deleteFoodItem() {
        // Logic to delete food item, Implemented in FoodItemServlet
    }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%s,%b,%d",
                foodId, name, description, price, categoryId, availability, orderCount);
    }

    public static FoodItem fromCSV(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length >= 7) {
            return new FoodItem(
                    data[0], data[1], data[2],
                    Double.parseDouble(data[3]),
                    data[4],
                    Boolean.parseBoolean(data[5]),
                    Integer.parseInt(data[6])
            );
        } else {
            return new FoodItem(
                    data[0], data[1], data[2],
                    Double.parseDouble(data[3]),
                    data[4],
                    Boolean.parseBoolean(data[5])
            );
        }
    }
}