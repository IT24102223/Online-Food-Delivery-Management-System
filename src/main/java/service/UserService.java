package service;

import model.User;
import model.Customer;
import model.Admin;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private static final String FILE_PATH = "WEB-INF/resources/data/users.txt";
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final ServletContext servletContext;

    public UserService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("users.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromCSV(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading users.txt", e);
            throw e;
        }
        return users;
    }

    public User getUserById(String userId) throws IOException {
        return getAllUsers().stream()
                .filter(user -> user.getUserID().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public void registerUser(User user) throws IOException {
        List<User> users = getAllUsers();
        if (users.stream().anyMatch(existing -> existing.getUserID().equals(user.getUserID()))) {
            throw new IllegalArgumentException("User ID " + user.getUserID() + " already exists.");
        }
        if (users.stream().anyMatch(existing -> existing.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("Email " + user.getEmail() + " is already in use.");
        }
        users.add(user);
        LOGGER.info("Adding user to list: " + user.toCSV());
        saveAllUsers(users);
        LOGGER.info("Successfully registered user " + user.getUserID());
    }

    public void updateUser(User updatedUser) throws IOException {
        List<User> users = getAllUsers();
        boolean updated = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserID().equals(updatedUser.getUserID())) {
                users.set(i, updatedUser);
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IOException("User with ID " + updatedUser.getUserID() + " not found");
        }
        saveAllUsers(users);
        LOGGER.info("Successfully updated user " + updatedUser.getUserID());
    }

    public void deleteUser(String userId) throws IOException {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getUserID().equals(userId));
        saveAllUsers(users);
        LOGGER.info("Successfully deleted user " + userId);
    }

    public User login(String email, String password) throws IOException {
        return getAllUsers().stream()
                .filter(user -> user.login(email, password))
                .findFirst()
                .orElse(null);
    }

    private void saveAllUsers(List<User> users) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            LOGGER.info("Created parent directories for " + realPath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (User user : users) {
                LOGGER.info("Writing user to file: " + user.toCSV());
                writer.write(user.toCSV());
                writer.newLine();
            }
            LOGGER.info("Successfully wrote " + users.size() + " users to users.txt at " + realPath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to users.txt: " + e.getMessage(), e);
            throw e;
        }
    }
}