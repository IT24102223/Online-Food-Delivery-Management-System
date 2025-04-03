package model;

public class FoodItem {
    private String foodId;
    private String name;
    private String description;
    private double price;
    private String categoryId;
    private boolean availability;

    public FoodItem(String foodId, String name, String description,
                    double price, String categoryId, boolean availability) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
        this.availability = availability;
    }

    // Getters & Setters
    public String getFoodId() { return foodId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategoryId() { return categoryId; }
    public boolean isAvailable() { return availability; }

    public String toCSV() {
        return String.format("%s,%s,%s,%.2f,%s,%b",
                foodId, name, description, price, categoryId, availability);
    }

    public static FoodItem fromCSV(String csvLine) {
        String[] data = csvLine.split(",");
        return new FoodItem(
                data[0], data[1], data[2],
                Double.parseDouble(data[3]),
                data[4],
                Boolean.parseBoolean(data[5])
        );
    }
}