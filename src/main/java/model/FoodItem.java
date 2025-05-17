package model;

public class FoodItem {
    private String foodId;
    private String name;
    private String categoryId;
    private double price;
    private boolean available;

    public FoodItem(String foodId, String name, String categoryId, double price, boolean available) {
        this.foodId = foodId;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.available = available;
    }

    // Getters and Setters
    public String getFoodId() { return foodId; }
    public String getCategoryId() { return categoryId; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
}