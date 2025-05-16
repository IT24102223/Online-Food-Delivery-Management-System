package model;

import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;

public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
    private boolean isActive;
    private final String role;

    public User(String username, String password, String email, String phone, String address, String role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isActive = true;
        this.role = role;
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean authenticate(String password) {
        return BCrypt.checkpw(password, this.password) && isActive;
    }

    public void updateProfile(String username, String email, String phone, String address) {
        if (username != null && !username.trim().isEmpty()) this.username = username;
        if (email != null && !email.trim().isEmpty()) this.email = email;
        if (phone != null && !phone.trim().isEmpty()) this.phone = phone;
        if (address != null && !address.trim().isEmpty()) this.address = address;
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (authenticate(currentPassword)) {
            this.password = hashPassword(newPassword);
        } else {
            throw new IllegalArgumentException("Current password is incorrect");
        }
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public boolean isActive() { return isActive; }
    public String getRole() { return role; }

    protected void setUserId(String userId) { this.userId = userId; }
    protected void setActive(boolean active) { this.isActive = active; }

    public abstract String toCSV();
}