package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.Customer;
import model.Admin;
import service.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/user", "/admin/users"})
public class UserServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");

        if ("/admin/users".equals(servletPath)) {
            if ("edit".equals(action)) {
                showEditForm(request, response);
            } else if ("delete".equals(action)) {
                deleteUser(request, response);
            } else {
                listUsers(request, response);
            }
        } else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } else if ("profile".equals(action)) {
                showProfile(request, response);
            } else if ("logout".equals(action)) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } else if ("viewHistory".equals(action)) {
                viewOrderHistory(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        LOGGER.info("Received POST request to " + servletPath + " with action: " + action);

        if ("login".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            login(request, response);
        } else if ("register".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            LOGGER.info("Processing register action");
            register(request, response);
        } else if ("update".equals(action)) {
            updateProfile(request, response);
        } else if ("delete".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            deleteAccount(request, response);
        } else if ("/admin/users".equals(servletPath)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if ("add".equals(action)) {
                addUser(request, response);
            } else if ("update".equals(action)) {
                updateUser(request, response);
            }
        } else {
            LOGGER.warning("Invalid action received: " + action);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            sendJsonResponse(response, false, "Invalid action");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/user/adminUserManagement.jsp").forward(request, response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error listing users: " + e.getMessage(), e);
            throw new ServletException("Unable to list users", e);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("id");
        try {
            User user = userService.getUserById(userId);
            if (user == null) {
                LOGGER.warning("User not found: " + userId);
                request.setAttribute("error", "User not found");
            } else {
                request.setAttribute("user", user);
            }
            request.getRequestDispatcher("/WEB-INF/views/user/adminUserManagement.jsp").forward(request, response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user: " + e.getMessage(), e);
            throw new ServletException("Error retrieving user", e);
        }
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userID = request.getParameter("userID");
            String role = request.getParameter("role");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");

            User user;
            if ("Customer".equals(role)) {
                user = new Customer(userID, name, email, password, phoneNumber, address);
            } else if ("Admin".equals(role)) {
                user = new Admin(userID, name, email, password, phoneNumber, address);
            } else {
                throw new IllegalArgumentException("Invalid role specified.");
            }

            userService.registerUser(user);
            sendJsonResponse(response, true, null);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error adding user: " + e.getMessage(), e);
            sendJsonResponse(response, false, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add user: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Failed to add user due to server error.");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userID = request.getParameter("userID");
            String role = request.getParameter("role");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");

            User user;
            if ("Customer".equals(role)) {
                user = new Customer(userID, name, email, password, phoneNumber, address);
            } else if ("Admin".equals(role)) {
                user = new Admin(userID, name, email, password, phoneNumber, address);
            } else {
                throw new IllegalArgumentException("Invalid role specified.");
            }

            userService.updateUser(user);
            sendJsonResponse(response, true, null);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error updating user: " + e.getMessage(), e);
            sendJsonResponse(response, false, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update user: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Failed to update user due to server error.");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("id");
        try {
            userService.deleteUser(userId);
            sendJsonResponse(response, true, null);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete user: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Failed to delete user due to server error.");
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            User user = userService.login(email, password);
            if (user == null) {
                sendJsonResponse(response, false, "Invalid email or password.");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                String redirectUrl = "Admin".equals(user.getRole()) ?
                        request.getContextPath() + "/admin/users" :
                        request.getContextPath() + "/food-items";
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", true);
                jsonResponse.put("redirectUrl", redirectUrl);
                try (PrintWriter out = response.getWriter()) {
                    out.println(jsonResponse.toString());
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Login error: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Login failed due to server error.");
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userID = UUID.randomUUID().toString();
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");

            LOGGER.info("Registering user with ID: " + userID + ", Email: " + email);

            User user = new Customer(userID, name, email, password, phoneNumber, address);
            userService.registerUser(user);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            String redirectUrl = request.getContextPath() + "/food-items";
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            jsonResponse.put("redirectUrl", redirectUrl);
            try (PrintWriter out = response.getWriter()) {
                out.println(jsonResponse.toString());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Registration error: " + e.getMessage(), e);
            sendJsonResponse(response, false, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Registration error: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Registration failed due to server error: " + e.getMessage());
        }
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        LOGGER.info("showProfile called, user: " + (user != null ? user.getName() : "null"));
        if (user == null) {
            LOGGER.warning("No user found in session, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.setAttribute("user", user);
        try {
            String forwardPath = "/profile.jsp";
            LOGGER.info("Attempting to forward to " + forwardPath);
            request.getRequestDispatcher(forwardPath).forward(request, response);
            LOGGER.info("Successfully forwarded to " + forwardPath);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to forward to profile.jsp: " + e.getMessage() + ", cause: " + (e.getCause() != null ? e.getCause().getMessage() : "none"), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load profile page: " + e.getMessage());
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String password = user.getPassword(); // Preserve existing password

            user.updateProfile(name, email, password, phone, address);
            userService.updateUser(user);
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/user?action=profile&updated=true");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Profile update error: " + e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + "/user?action=profile&updated=false&error=Failed to update profile due to server error.");
        }
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            sendJsonResponse(response, false, "User not logged in.");
            return;
        }

        try {
            userService.deleteUser(user.getUserID());
            session.invalidate();
            sendJsonResponse(response, true, null);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Account deletion error: " + e.getMessage(), e);
            sendJsonResponse(response, false, "Failed to delete account due to server error.");
        }
    }

    private void viewOrderHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.getRequestDispatcher("/orderHistory.jsp").forward(request, response);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, boolean success, String errorMessage)
            throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        if (errorMessage != null) {
            jsonResponse.put("error", errorMessage);
        }
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonResponse.toString());
        }
    }
}