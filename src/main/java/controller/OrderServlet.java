package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
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
                    logger.warn("Error parsing food item CSV: {}", e.getMessage());
                }
            }
            logger.info("Loaded {} food items in OrderServlet", foodItems.size());
        } catch (IOException e) {
            logger.error("Failed to load food items: {}", e.getMessage());
            throw new ServletException("Failed to load food items", e);
        }
        orderService = new OrderService();
    }

    private Customer checkSessionAndGetCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer == null) {
            logger.warn("No user session found, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return null;
        }
        return customer;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = checkSessionAndGetCustomer(request, response);
        if (customer == null) return;

        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

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
                request.setAttribute("orders", orders != null ? orders : new ArrayList<>());
                request.getRequestDispatcher("/WEB-INF/views/orders/list.jsp").forward(request, response);
                break;
            case "/details":
                String orderId = request.getParameter("id");
                if (orderId == null) {
                    logger.warn("Order ID missing in details request");
                    request.setAttribute("error", "Please provide an order ID.");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                Order order = orderService.getOrderById(orderId, customer, getServletContext());
                if (order == null || !order.getCustomer().getUserId().equals(customer.getUserId())) {
                    logger.warn("Order {} not found or unauthorized for user {}", orderId, customer.getUserId());
                    request.setAttribute("error", "Order not found or you are not authorized to view it.");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/views/orders/details.jsp").forward(request, response);
                break;
            default:
                logger.warn("Invalid path: {}", pathInfo);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid order path");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = checkSessionAndGetCustomer(request, response);
        if (customer == null) return;

        String pathInfo = request.getPathInfo() != null ? request.getPathInfo() : "/";

        switch (pathInfo) {
            case "/add-to-cart":
                String itemId = request.getParameter("itemId");
                String quantityStr = request.getParameter("quantity");
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid quantity for item {}: {}", itemId, e.getMessage());
                    request.setAttribute("error", "Please enter a valid quantity.");
                    request.setAttribute("foodItems", foodItems);
                    request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                    return;
                }
                FoodItem foodItem = foodItems.stream()
                        .filter(f -> f.getFoodId().equals(itemId))
                        .findFirst()
                        .orElse(null);
                if (foodItem == null || !foodItem.isAvailable()) {
                    logger.warn("Item {} not available", itemId);
                    request.setAttribute("error", "Selected item is not available.");
                    request.setAttribute("foodItems", foodItems);
                    request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                    return;
                }
                customer.getCart().addItem(foodItem, quantity);
                customer.getCart().save(getServletContext());
                logger.info("Added item {} (quantity: {}) to cart for user {}", itemId, quantity, customer.getUserId());
                response.sendRedirect(request.getContextPath() + "/order/menu");
                break;
            case "/create":
                try {
                    customer.placeOrder();
                    request.getSession().setAttribute("lastOrderId",
                            customer.getOrderHistory().get(customer.getOrderHistory().size() - 1).getOrderId());
                    logger.info("Order created for user {}", customer.getUserId());
                    response.sendRedirect(request.getContextPath() + "/order/confirmation");
                } catch (Exception e) {
                    logger.error("Failed to place order for user {}: {}", customer.getUserId(), e.getMessage());
                    request.setAttribute("error", "Unable to place order: " + e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            case "/cancel":
                String cancelOrderId = request.getParameter("id");
                if (cancelOrderId == null) {
                    logger.warn("Order ID missing in cancel request");
                    request.setAttribute("error", "Please provide an order ID.");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                try {
                    orderService.cancelOrder(cancelOrderId, customer.getUserId(), getServletContext());
                    logger.info("Order {} cancelled by user {}", cancelOrderId, customer.getUserId());
                    response.sendRedirect(request.getContextPath() + "/order/list");
                } catch (IllegalStateException | IllegalArgumentException e) {
                    logger.warn("Failed to cancel order {}: {}", cancelOrderId, e.getMessage());
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            case "/update":
                String updateOrderId = request.getParameter("id");
                String newStatus = request.getParameter("status");
                if (updateOrderId == null || newStatus == null) {
                    logger.warn("Order ID or status missing in update request");
                    request.setAttribute("error", "Please provide both order ID and status.");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                try {
                    Order.Status status = Order.Status.valueOf(newStatus);
                    orderService.updateOrderStatus(updateOrderId, customer.getUserId(), status, getServletContext());
                    logger.info("Order {} status updated to {} by user {}", updateOrderId, status, customer.getUserId());
                    response.sendRedirect(request.getContextPath() + "/order/details?id=" + updateOrderId);
                } catch (IllegalArgumentException e) {
                    logger.warn("Invalid status {} for order {}", newStatus, updateOrderId);
                    request.setAttribute("error", "Invalid order status provided.");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                } catch (IllegalStateException | SecurityException e) {
                    logger.warn("Failed to update order {}: {}", updateOrderId, e.getMessage());
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            default:
                logger.warn("Invalid POST path: {}", pathInfo);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid order path");
        }
    }
}