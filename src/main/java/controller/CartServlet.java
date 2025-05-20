package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Cart;
import model.FoodItem;
import model.User;
import service.CartService;
import java.util.List;
import java.io.IOException;


@WebServlet("/cart")

public class CartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Retrieve or create cart using userID only
        Cart cart = cartService.getOrCreateCart(session, user.getUserID());

        // Calculate totals
        double subtotal = cartService.getTotalAmount(session);
        double discount = subtotal * 0.20; // 20% discount
        double deliveryFee = 150.0;
        double total = subtotal - discount + deliveryFee;

        request.setAttribute("cartItems", cartService.getCartItems(session));
        request.setAttribute("subtotal", String.format("%.2f", subtotal));
        request.setAttribute("discount", String.format("%.2f", discount));
        request.setAttribute("deliveryFee", "150.00 LKR");
        request.setAttribute("total", String.format("%.2f", total));

        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String foodId = request.getParameter("foodId");
        List<Cart.CartItem> cartItems = cartService.getCartItems(session);

        if ("add".equals(action) && foodId != null) {
            FoodItem foodItem = cartService.getFoodItemById(foodId);
            if (foodItem != null && foodItem.isAvailable()) {
                cartService.addItem(session, foodItem);
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        } else if ("clear".equals(action)) {
            cartService.clearCart(session);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            int index = Integer.parseInt(request.getParameter("index"));
            if ("update".equals(action) && index >= 0 && index < cartItems.size()) {
                int change = Integer.parseInt(request.getParameter("change"));
                Cart.CartItem cartItem = cartItems.get(index);
                int newQuantity = Math.max(1, cartItem.quantity + change);
                cartService.updateQuantity(session, cartItem.foodItem, newQuantity);
            } else if ("delete".equals(action) && index >= 0 && index < cartItems.size()) {
                Cart.CartItem cartItem = cartItems.get(index);
                cartService.removeItem(session, cartItem.foodItem);
            }
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

}
