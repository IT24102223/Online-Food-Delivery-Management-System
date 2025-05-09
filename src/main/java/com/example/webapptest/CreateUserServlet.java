package com.example.webapptest;

import com.example.webapptest.model.User;
import com.example.webapptest.service.UserService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CreateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User(
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("phone"),
                request.getParameter("password"),
                request.getParameter("address"),
                "user"
        );

        UserService userService = new UserService();
        userService.saveUser(user);

        response.sendRedirect("success.html");
    }
}
