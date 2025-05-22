package model;

public class Admin extends User {
    public Admin(String userID, String name, String email, String password, String phoneNumber, String address) {
        super(userID, name, email, password, phoneNumber, address);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public void manageUsers() {
        // Logic to manage users, implemented in UserServlet
    }

    public void manageFoodItems() {
        // Logic to manage food items, implemented in FoodItemServlet
    }

    public void manageCategories() {
        // Logic to manage categories, implemented in FoodItemServlet
    }

    public void manageQueue() {
        // Logic to manage queue, to be implemented
    }

}
