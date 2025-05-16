package model;

public class OrderItem {
    private final FoodItem foodItem;
    private int quantity;
    private String specialInstructions;

    public OrderItem(FoodItem foodItem, int quantity) {
        this(foodItem, quantity, "");
    }

    public OrderItem(FoodItem foodItem, int quantity, String specialInstructions) {
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
    }

    public double getSubtotal() {
        return foodItem.getPrice() * quantity;
    }

    // Getters & Setters
    public FoodItem getFoodItem() { return foodItem; }
    public int getQuantity() { return quantity; }
    public String getSpecialInstructions() { return specialInstructions; }

    public void setQuantity(int quantity) {
        if (quantity > 0) this.quantity = quantity;
    }
    public void setSpecialInstructions(String instructions) {
        this.specialInstructions = instructions;
    }
}