package com.example.webapptest;

import com.example.webapptest.model.User;
import com.example.webapptest.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ReadUserServlet")
public class ReadUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserService userService = new UserService();
        User user = userService.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("email", user.getEmail());
            session.setAttribute("role", user.getRole());
            session.setAttribute("loggedInUser", user); // Store full user for profile

            if (user.getEmail().equalsIgnoreCase("admin@food.com")) {
                // Redirect admin to admin dashboard
                response.sendRedirect("admindashboard.html");
            } else {
                // Regular user
                response.sendRedirect("dashboard.html");
            }

        } else {
            response.sendRedirect("login.html?error=1");
        }
    }
}