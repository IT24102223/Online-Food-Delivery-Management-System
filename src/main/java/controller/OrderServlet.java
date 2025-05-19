package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.OrderService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {
    private List<FoodItem> foodItems;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        foodItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResourceAsStream("/WEB-INF/resources/data/menu.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    foodItems.add(FoodItem.fromCSV(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing food item CSV: " + e.getMessage());
                }
            }
            System.out.println("Loaded " + foodItems.size() + " food items in OrderServlet");
        } catch (IOException e) {
            throw new ServletException("Failed to load food items", e);
        }
        orderService = new OrderService();
    }

    private Customer checkSessionAndGetCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer == null) {
            System.err.println("No user session found, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return null;
        }
        return customer;
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"&']", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = checkSessionAndGetCustomer(request, response);
        if (customer == null) return;

        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

        try {
            switch (pathInfo) {
                case "/menu":
                    request.setAttribute("foodItems", foodItems);
                    request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                    break;
                case "/create":
                    request.getRequestDispatcher("/WEB-INF/views/orders/create.jsp").forward(request, response);
                    break;
                case "/confirmation":
                    request.getRequestDispatcher("/WEB-INF/views/orders/confirmation.jsp").forward(request, response);
                    break;
                case "/list":
                    List<Order> orders = customer.getOrderHistory();
                    if (orders == null) orders = new ArrayList<>();
                    String search = sanitizeInput(request.getParameter("search"));
                    if (search != null && !search.isEmpty()) {
                        final String searchLower = search.toLowerCase();
                        orders = orders.stream()
                                .filter(order -> order.getOrderId().toLowerCase().contains(searchLower) ||
                                        order.getStatus().toString().toLowerCase().contains(searchLower))
                                .collect(Collectors.toList());
                    }
                    String sort = sanitizeInput(request.getParameter("sort"));
                    if (sort != null) {
                        switch (sort) {
                            case "date_asc":
                                orders.sort(Comparator.comparing(Order::getOrderDate));
                                break;
                            case "date_desc":
                                orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
                                break;
                            case "status":
                                orders.sort(Comparator.comparing(order -> order.getStatus().toString()));
                                break;
                        }
                    }
                    request.setAttribute("orders", orders);
                    request.getRequestDispatcher("/WEB-INF/views/orders/list.jsp").forward(request, response);
                    break;
                case "/details":
                    String orderId = sanitizeInput(request.getParameter("id"));
                    if (orderId == null || orderId.isEmpty()) {
                        request.setAttribute("error", "Please provide a valid order ID.");
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                        return;
                    }
                    Order order = orderService.getOrderById(orderId, customer, getServletContext());
                    if (order == null || !order.getCustomer().getUserId().equals(customer.getUserId())) {
                        request.setAttribute("error", "Order not found or you are not authorized to view it.");
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                        return;
                    }
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/WEB-INF/views/orders/details.jsp").forward(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid order path");
            }
        } catch (ServletException e) {
            System.err.println("JSP not found: " + e.getMessage());
            request.setAttribute("error", "Page not available. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = checkSessionAndGetCustomer(request, response);
        if (customer == null) return;

        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

        try {
            switch (pathInfo) {
                case "/add-to-cart":
                    String itemId = sanitizeInput(request.getParameter("itemId"));
                    String quantityStr = sanitizeInput(request.getParameter("quantity"));
                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
                    } catch (NumberFormatException | IllegalArgumentException e) {
                        request.setAttribute("error", "Please enter a valid quantity (positive number).");
                        request.setAttribute("foodItems", foodItems);
                        request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                        return;
                    }
                    FoodItem foodItem = foodItems.stream()
                            .filter(f -> f.getFoodId().equals(itemId))
                            .findFirst()
                            .orElse(null);
                    if (foodItem == null || !foodItem.isAvailable()) {
                        request.setAttribute("error", "Selected item is not available.");
                        request.setAttribute("foodItems", foodItems);
                        request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                        return;
                    }
                    customer.getCart().addItem(foodItem, quantity);
                    customer.getCart().save(getServletContext());
                    System.out.println("Added item " + itemId + " (quantity: " + quantity + ") to cart for user " + customer.getUserId());
                    response.sendRedirect(request.getContextPath() + "/order/menu");
                    break;
                case "/create":
                    try {
                        customer.placeOrder();
                        String lastOrderId = customer.getOrderHistory().get(customer.getOrderHistory().size() - 1).getOrderId();
                        request.getSession().setAttribute("lastOrderId", lastOrderId);
                        System.out.println("Order created for user " + customer.getUserId() + ", order ID: " + lastOrderId);
                        response.sendRedirect(request.getContextPath() + "/order/confirmation");
                    } catch (IllegalStateException e) {
                        request.setAttribute("error", "Cannot place order: " + e.getMessage());
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    }
                    break;
                case "/cancel":
                    String cancelOrderId = sanitizeInput(request.getParameter("id"));
                    if (cancelOrderId == null || cancelOrderId.isEmpty()) {
                        request.setAttribute("error", "Please provide a valid order ID.");
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                        return;
                    }
                    try {
                        orderService.cancelOrder(cancelOrderId, customer.getUserId(), getServletContext());
                        System.out.println("Order " + cancelOrderId + " cancelled by user " + customer.getUserId());
                        response.sendRedirect(request.getContextPath() + "/order/list");
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        request.setAttribute("error", e.getMessage());
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    }
                    break;
                case "/update":
                    String updateOrderId = sanitizeInput(request.getParameter("id"));
                    String newStatus = sanitizeInput(request.getParameter("status"));
                    if (updateOrderId == null || updateOrderId.isEmpty() || newStatus == null || newStatus.isEmpty()) {
                        request.setAttribute("error", "Please provide both a valid order ID and status.");
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                        return;
                    }
                    try {
                        Order.Status status = Order.Status.valueOf(newStatus.toUpperCase());
                        orderService.updateOrderStatus(updateOrderId, customer.getUserId(), status, getServletContext());
                        System.out.println("Order " + updateOrderId + " status updated to " + status + " by user " + customer.getUserId());
                        response.sendRedirect(request.getContextPath() + "/order/details?id=" + updateOrderId);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute("error", "Invalid order status provided.");
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    } catch (IllegalStateException | SecurityException e) {
                        request.setAttribute("error", e.getMessage());
                        request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    }
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid order path");
            }
        } catch (ServletException e) {
            System.err.println("JSP not found: " + e.getMessage());
            request.setAttribute("error", "Page not available. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
        }
    }
}