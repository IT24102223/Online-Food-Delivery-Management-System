package model;

public class Customer extends User {
    public Customer(String userID, String name, String email, String password, String phoneNumber, String address) {
        super(userID, name, email, password, phoneNumber, address);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    public void browseMenu() {
        // Logic to browse menu, implemented in FoodItemServlet
    }

    public void placeOrder() {
        // Logic to place order, implemented in OrderServlet (to be created)
    }


}