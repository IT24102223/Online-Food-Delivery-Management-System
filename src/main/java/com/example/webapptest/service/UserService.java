package com.example.webapptest.service;

import com.example.webapptest.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String FILE_PATH = "C:\\Users\\User\\Documents\\SLIIT\\OOP\\Labsheets\\Webapp-Test\\users.txt";

    public void saveUser(User user) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            out.println(user.getName() + "," + user.getEmail() + "," + user.getPhone() + "," +
                    user.getPassword() + "," + user.getAddress() + "," + user.getRole());
        }
    }

    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        }
        return users;
    }

    public User findUser(String email, String password) throws IOException {
        for (User user : getAllUsers()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void updateUser(User updatedUser) throws IOException {
        List<User> users = getAllUsers();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                if (user.getEmail().equals(updatedUser.getEmail())) {
                    writer.println(updatedUser.getName() + "," + user.getEmail() + "," +
                            updatedUser.getPhone() + "," + updatedUser.getPassword() + "," +
                            updatedUser.getAddress() + "," + updatedUser.getRole());
                } else {
                    writer.println(user.getName() + "," + user.getEmail() + "," + user.getPhone() + "," +
                            user.getPassword() + "," + user.getAddress() + "," + user.getRole());
                }
            }
        }
    }

    public void deleteUser(String email) throws IOException {
        List<User> users = getAllUsers();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User user : users) {
                if (!user.getEmail().equals(email)) {
                    writer.println(user.getName() + "," + user.getEmail() + "," + user.getPhone() + "," +
                            user.getPassword() + "," + user.getAddress() + "," + user.getRole());
                }
            }
        }
    }

    public User getUserByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[1].equals(email)) {
                    return new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }
}