package model;

public abstract class User {
    protected String userID;
    protected String name;
    protected String email;
    protected String password;
    protected String phoneNumber;
    protected String address;

    public User(String userID, String name, String email, String password, String phoneNumber, String address) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // Abstract methods to be implemented by subclasses
    public abstract String getRole();

    // Common methods
    public void register() {
        // To be implemented in UserService
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void updateProfile(String name, String email, String password, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void deleteAccount() {
        // To be implemented in UserService
    }

    public void viewOrderHistory() {
        // To be implemented in UserService
    }

    // CSV serialization
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                userID, getRole(), name, email, password, phoneNumber, address);
    }

    public static User fromCSV(String csvLine) {
        String[] data = csvLine.split(",");
        if ("Customer".equals(data[1])) {
            return new Customer(data[0], data[2], data[3], data[4], data[5], data[6]);
        } else if ("Admin".equals(data[1])) {
            return new Admin(data[0], data[2], data[3], data[4], data[5], data[6]);
        }
        return null;
    }
}