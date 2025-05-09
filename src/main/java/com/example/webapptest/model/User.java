package com.example.webapptest.model;

public class User {
    private String name, email, phone, password, address, role;

    public User(String name, String email, String phone, String password, String address, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public String getRole() { return role; }

    // Optional: Setters for updating
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(String address) { this.address = address; }
}