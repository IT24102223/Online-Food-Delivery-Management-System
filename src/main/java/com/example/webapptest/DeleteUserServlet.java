package com.example.webapptest;

import com.example.webapptest.service.UserService;
import jakarta.servlet.http.*;

import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");

        UserService userService = new UserService();
        userService.deleteUser(email);

        response.sendRedirect("login.html");
    }
}