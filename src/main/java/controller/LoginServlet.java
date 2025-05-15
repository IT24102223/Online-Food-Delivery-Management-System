package controller;

import model.Customer;
import model.OrderQueue;
import model.User;
import model.FoodItem;
import service.OrderQueueService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String USERS_FILE = "/WEB-INF/resources/data/users.txt";
    private static final String CART_FILE = "/WEB-INF/resources/data/cart.txt";
    private static final String MENU_FILE = "/WEB-INF/resources/data/menu.txt";
    private final OrderQueueService orderQueueService;
    private List<FoodItem> foodItems;

    public LoginServlet() {
        this.orderQueueService = new OrderQueueService();
        this.foodItems = new ArrayList<>();
    }

    @Override
    public void init() throws ServletException {
        loadFoodItems();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        List<User> users = loadUsers();
        User user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.authenticate(password))
                .findFirst()
                .orElse(null);

        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            loadCartForUser(customer);
            HttpSession session = request.getSession();
            session.setAttribute("user", customer);
            response.sendRedirect(request.getContextPath() + "/order/list");
        } else {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        OrderQueue orderQueue = new OrderQueue();
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getServletContext().getResourceAsStream(USERS_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8 && data[6].equals("CUSTOMER")) {
                    Customer customer = Customer.fromCSV(line, orderQueue, getServletContext());
                    users.add(customer);
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        // Removed orderQueueService.initialize() as Customer initializes its own OrderQueue
        return users;
    }

    private void loadFoodItems() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getServletContext().getResourceAsStream(MENU_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String itemId = data[0];
                    String name = data[1];
                    double price = Double.parseDouble(data[2]);
                    boolean available = Boolean.parseBoolean(data[3]);
                    String description = "No description available";
                    String categoryId = "default";
                    FoodItem foodItem = new FoodItem(itemId, name, description, price, categoryId, available);
                    foodItems.add(foodItem);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading food items in LoginServlet: " + e.getMessage());
        }
    }

    private void loadCartForUser(Customer customer) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getServletContext().getResourceAsStream(CART_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(customer.getUsername())) {
                    String itemId = data[1];
                    int quantity = Integer.parseInt(data[2]);
                    FoodItem foodItem = foodItems.stream()
                            .filter(item -> item.getFoodId().equals(itemId))
                            .findFirst()
                            .orElse(null);
                    if (foodItem != null && foodItem.isAvailable()) {
                        customer.getCart().addItem(foodItem, quantity);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cart for user " + customer.getUsername() + ": " + e.getMessage());
        }
    }
}