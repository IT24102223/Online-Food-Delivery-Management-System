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
import java.util.List;

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
        } catch (IOException e) {
            throw new ServletException("Failed to load food items", e);
        }
        orderService = new OrderService();
    }

    private Customer checkSessionAndGetCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer == null) {
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

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

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
                    request.setAttribute("error", "Order ID is required");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                Order order = orderService.getOrderById(orderId, customer, getServletContext());
                if (order == null || !order.getCustomer().getUserId().equals(customer.getUserId())) {
                    request.setAttribute("error", "Order not found");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/views/orders/details.jsp").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = checkSessionAndGetCustomer(request, response);
        if (customer == null) return;

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

        switch (pathInfo) {
            case "/add-to-cart":
                String itemId = request.getParameter("itemId");
                String quantityStr = request.getParameter("quantity");
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
                } catch (IllegalArgumentException e) {
                    request.setAttribute("error", "Invalid quantity");
                    request.setAttribute("foodItems", foodItems);
                    request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                    return;
                }
                FoodItem foodItem = foodItems.stream()
                        .filter(f -> f.getFoodId().equals(itemId))
                        .findFirst()
                        .orElse(null);
                if (foodItem == null || !foodItem.isAvailable()) {
                    request.setAttribute("error", "Item not available");
                    request.setAttribute("foodItems", foodItems);
                    request.getRequestDispatcher("/WEB-INF/views/orders/menu.jsp").forward(request, response);
                    return;
                }
                customer.getCart().addItem(foodItem, quantity);
                customer.getCart().save(getServletContext());
                response.sendRedirect(request.getContextPath() + "/order/menu");
                break;
            case "/create":
                try {
                    customer.placeOrder();
                    request.getSession().setAttribute("lastOrderId", customer.getOrderHistory().get(customer.getOrderHistory().size() - 1).getOrderId());
                    response.sendRedirect(request.getContextPath() + "/order/confirmation");
                } catch (Exception e) {
                    request.setAttribute("error", "Failed to place order: " + e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            case "/cancel":
                String cancelOrderId = request.getParameter("id");
                if (cancelOrderId == null) {
                    request.setAttribute("error", "Order ID is required");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                try {
                    orderService.cancelOrder(cancelOrderId, customer.getUserId(), getServletContext());
                    response.sendRedirect(request.getContextPath() + "/order/list");
                } catch (IllegalStateException | IllegalArgumentException e) {
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            case "/update":
                String updateOrderId = request.getParameter("id");
                String newStatus = request.getParameter("status");
                if (updateOrderId == null || newStatus == null) {
                    request.setAttribute("error", "Order ID and status are required");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                    return;
                }
                try {
                    Order.Status status = Order.Status.valueOf(newStatus);
                    orderService.updateOrderStatus(updateOrderId, customer.getUserId(), status, getServletContext());
                    response.sendRedirect(request.getContextPath() + "/order/details?id=" + updateOrderId);
                } catch (IllegalArgumentException e) {
                    request.setAttribute("error", "Invalid status");
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                } catch (IllegalStateException | SecurityException e) {
                    request.setAttribute("error", e.getMessage());
                    request.getRequestDispatcher("/WEB-INF/views/orders/error.jsp").forward(request, response);
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}