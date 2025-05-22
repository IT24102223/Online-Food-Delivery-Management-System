package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.FoodItem;
import model.Order;
import model.User;
import service.FoodItemService;
import service.OrderService;
import service.OrderQueueService;
import service.CartService;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/order", "/myorders"})
public class OrderServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private OrderService orderService;
    private FoodItemService foodItemService;
    private CartService cartService;
    private OrderQueueService orderQueueService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderService(getServletContext());
        this.foodItemService = new FoodItemService(getServletContext());
        this.cartService = new CartService(getServletContext());
        this.orderQueueService = new OrderQueueService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String path = request.getServletPath();
        if ("/order".equals(path)) {
            Cart cart = cartService.getOrCreateCart(session, user.getUserID());
            List<Cart.CartItem> cartItems = cartService.getCartItems(session);
            double subtotal = cartService.getTotalAmount(session);
            double discount = subtotal * 0.20;
            double deliveryFee = 150.0;
            double total = subtotal - discount + deliveryFee;

            request.setAttribute("user", user);
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("subtotal", String.format("%.2f", subtotal));
            request.setAttribute("discount", String.format("%.2f", discount));
            request.setAttribute("deliveryFee", "150.00 LKR");
            request.setAttribute("total", String.format("%.2f", total));
            request.getRequestDispatcher("/order.jsp").forward(request, response);
        } else if ("/myorders".equals(path)) {
            try {
                List<Order> allOrders = orderService.getAllOrders();
                List<Order> userOrders = allOrders.stream()
                        .filter(order -> order.getUserId().equals(user.getUserID()))
                        .toList();
                request.setAttribute("orders", userOrders);
                request.getRequestDispatcher("/myorders.jsp").forward(request, response);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error fetching orders: " + e.getMessage(), e);
                response.sendRedirect(request.getContextPath() + "/error.jsp?message=Failed to load orders");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null || cart == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if ("place".equals(action)) {
            try {
                String deliveryAddress = request.getParameter("deliveryAddress");
                if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
                    deliveryAddress = user.getAddress();
                }
                List<Cart.CartItem> cartItems = cart.getItems();
                List<FoodItem> orderItems = new ArrayList<>();
                double totalAmount = 0.0;

                for (Cart.CartItem cartItem : cartItems) {
                    FoodItem foodItem = foodItemService.getFoodItemById(cartItem.getFoodItem().getFoodId());
                    if (foodItem != null && foodItem.isAvailable()) {
                        orderItems.add(foodItem);
                        totalAmount += foodItem.getPrice() * cartItem.getQuantity();
                        foodItemService.incrementOrderCount(foodItem.getFoodId());
                    } else {
                        LOGGER.warning("Food item " + cartItem.getFoodItem().getFoodId() + " is unavailable or not found");
                    }
                }

                if (orderItems.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/order.jsp?error=No available items to order");
                    return;
                }

                String orderId = "ORDER" + UUID.randomUUID().toString().substring(0, 8);
                Date orderDate = new Date();
                String status = "Pending";

                Order order = new Order(orderId, user.getUserID(), orderItems, totalAmount, orderDate, deliveryAddress, status);
                orderService.placeOrder(order);
                orderQueueService.addToQueue(order); // Add the order to the queue

                cart.clearCart();
                session.setAttribute("cart", cart);

                response.sendRedirect(request.getContextPath() + "/order.jsp?orderPlaced=true");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error placing order: " + e.getMessage(), e);
                response.sendRedirect(request.getContextPath() + "/order.jsp?error=Failed to place order");
            }
        } else if ("updateStatus".equals(action)) {
            String orderId = request.getParameter("orderId");
            String newStatus = request.getParameter("status");
            try {
                orderService.updateOrderStatus(orderId, newStatus);
                response.sendRedirect(request.getContextPath() + "/myorders?statusUpdated=true");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error updating order status: " + e.getMessage(), e);
                response.sendRedirect(request.getContextPath() + "/myorders?error=Failed to update status");
            }
        } else if ("cancel".equals(action)) {
            String orderId = request.getParameter("orderId");
            try {
                orderService.cancelOrder(orderId);
                response.getWriter().write("success");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error cancelling order: " + e.getMessage(), e);
                response.getWriter().write("error");
            }
        }
    }
}