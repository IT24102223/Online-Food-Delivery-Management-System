package controller;

import model.FoodItem;
import service.FoodItemService;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/food-items")
public class FoodItemServlet extends HttpServlet {
    private FoodItemService foodItemService = new FoodItemService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            listFoodItems(request, response);
        } else {
            switch (action) {
                case "new":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteFoodItem(request, response);
                    break;
                default:
                    listFoodItems(request, response);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addFoodItem(request, response);
        } else if ("update".equals(action)) {
            updateFoodItem(request, response);
        } else {
            listFoodItems(request, response);
        }
    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        //request.setAttribute("foodItems", foodItems);
        //request.getRequestDispatcher("/WEB-INF/views/food/list.jsp").forward(request, response);

        List<FoodItem> popularItems = foodItemService.getPopularItems(); // Implement this in your service
        request.setAttribute("popularItems", popularItems);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/food/add.jsp").forward(request, response);
    }

    private void addFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FoodItem newItem = new FoodItem(
                request.getParameter("foodId"),
                request.getParameter("name"),
                request.getParameter("description"),
                Double.parseDouble(request.getParameter("price")),
                request.getParameter("categoryId"),
                Boolean.parseBoolean(request.getParameter("availability"))
        );
        foodItemService.addFoodItem(newItem);
        response.sendRedirect("food-items");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodId = request.getParameter("id");
        try {
            FoodItem foodItem = foodItemService.getAllFoodItems().stream()
                    .filter(item -> item.getFoodId().equals(foodId))
                    .findFirst()
                    .orElseThrow(() -> new ServletException("Food item not found"));
            request.setAttribute("foodItem", foodItem);
            request.getRequestDispatcher("/WEB-INF/views/food/edit.jsp").forward(request, response);
        } catch (IOException e) {
            throw new ServletException("Error retrieving food item", e);
        }
    }

    private void updateFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FoodItem updatedItem = new FoodItem(
                request.getParameter("foodId"),
                request.getParameter("name"),
                request.getParameter("description"),
                Double.parseDouble(request.getParameter("price")),
                request.getParameter("categoryId"),
                Boolean.parseBoolean(request.getParameter("availability"))
        );

        try {
            foodItemService.updateFoodItem(updatedItem);
            response.sendRedirect("food-items");
        } catch (IOException e) {
            throw new ServletException("Error updating food item", e);
        }
    }

    private void deleteFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodId = request.getParameter("id");
        try {
            foodItemService.deleteFoodItem(foodId);
            response.sendRedirect("food-items");
        } catch (IOException e) {
            throw new ServletException("Error deleting food item", e);
        }
    }

    // Implement similar methods for edit/update/delete
}