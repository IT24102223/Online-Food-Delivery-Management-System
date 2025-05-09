package com.example.webapptest;

import com.example.webapptest.model.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;

public class UpdateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect("login.html");
            return;
        }

        String email = loggedInUser.getEmail();
        String newName = request.getParameter("name");
        String newPhone = request.getParameter("phone");
        String newPassword = request.getParameter("password");
        String newAddress = request.getParameter("address");
        String role = loggedInUser.getRole();

        File inputFile = new File("C:\\Users\\User\\Documents\\SLIIT\\OOP\\Labsheets\\Webapp-Test\\users.txt");
        File tempFile = new File("C:\\Users\\User\\Documents\\SLIIT\\OOP\\Labsheets\\Webapp-Test\\tempusers.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 6 && parts[1].equals(email)) {
                writer.println(newName + "," + email + "," + newPhone + "," + newPassword + "," + newAddress + "," + role);
            } else {
                writer.println(line);
            }
        }
        reader.close();
        writer.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);

        User updatedUser = new User(newName, email, newPhone, newPassword, newAddress, role);
        session.setAttribute("loggedInUser", updatedUser);

        response.sendRedirect("profile.html");
    }
}
