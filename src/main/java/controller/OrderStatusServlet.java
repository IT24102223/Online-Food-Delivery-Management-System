package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.Order;
import service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/order/status")
public class OrderStatusServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Customer customer = (Customer) request.getSession().getAttribute("user");
        if (customer == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        String orderId = request.getParameter("id");
        if (orderId == null || orderId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID required");
            return;
        }

        Order order = orderService.getOrderById(orderId, customer, getServletContext());
        if (order == null || !order.getCustomer().getUserId().equals(customer.getUserId())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"status\": \"" + order.getStatus().toString() + "\"}");
        out.flush();
    }
}