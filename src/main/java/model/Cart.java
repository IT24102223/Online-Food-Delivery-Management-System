package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userID;
    private List<CartItem> items;

    public Cart(String userID) {
        this.userID = userID;
        this.items = new ArrayList<>();
    }

    // Nested class to represent an item in the cart
    public static class CartItem {
        public FoodItem foodItem;
        public int quantity;

        public CartItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // Add an item to the cart
    public void addItem(FoodItem foodItem) {
        for (CartItem item : items) {
            if (item.foodItem.getFoodId().equals(foodItem.getFoodId())) {
                item.quantity++;
                return;
            }
        }
        items.add(new CartItem(foodItem, 1));
    }

    // Remove an item from the cart
    public void removeItem(FoodItem foodItem) {
        items.removeIf(item -> item.foodItem.getFoodId().equals(foodItem.getFoodId()));
    }

    // Update the quantity of an item in the cart
    public void updateQuantity(FoodItem foodItem, int newQuantity) {
        for (CartItem item : items) {
            if (item.foodItem.getFoodId().equals(foodItem.getFoodId())) {
                if (newQuantity <= 0) {
                    items.remove(item);
                } else {
                    item.quantity = newQuantity;
                }
                return;
            }
        }
        if (newQuantity > 0) {
            items.add(new CartItem(foodItem, newQuantity));
        }
    }

    // Clear the cart
    public void clearCart() {
        items.clear();
    }

    // Get the list of items in the cart
    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    // Calculate the total amount
    public double getTotalAmount() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.foodItem.getPrice() * item.quantity;
        }
        return total;
    }

    // Getter for userID
    public String getUserID() {
        return userID;
    }
}