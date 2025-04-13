package controller;

import model.FoodItem;
import service.FoodItemService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@WebServlet("/food-items")
public class FoodItemServlet extends HttpServlet {
    private FoodItemService foodItemService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.foodItemService = new FoodItemService(getServletContext());
    }

    @Override
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

    @Override
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
        // Get pagination parameters, default to page 1 if invalid or not provided
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        } catch (NumberFormatException e) {
            page = 1; // Default to page 1 on invalid input
        }
        int itemsPerPage = 9; // Number of items per page (matches 3x3 grid layout)

        // Check for reset parameter
        boolean isReset = "true".equals(request.getParameter("reset"));
        String[] selectedCategories = isReset ? null : request.getParameterValues("categories"); // Clear categories if reset
        String priceRangeStr = isReset ? "500" : request.getParameter("priceRange"); // Default to 500 if reset
        String sortBy = isReset ? "highToLow" : request.getParameter("sortBy"); // Default to highToLow if reset

        // Parse price range
        double maxPrice;
        try {
            maxPrice = priceRangeStr != null && !priceRangeStr.trim().isEmpty() ? Double.parseDouble(priceRangeStr) : 500.0;
        } catch (NumberFormatException e) {
            maxPrice = 500.0; // Default to max if invalid
        }

        // Fetch paginated food items with filters
        List<FoodItem> allItems = foodItemService.getAllFoodItems(); // Get all items first
        List<FoodItem> filteredItems = foodItemService.filterFoodItems(allItems, selectedCategories, maxPrice); // Apply category and price filters

        // Apply sorting based on sortBy parameter, default to highToLow
        if (sortBy == null || sortBy.isEmpty() || "highToLow".equals(sortBy)) {
            filteredItems.sort(Comparator.comparingDouble(FoodItem::getPrice).reversed());
            sortBy = "highToLow"; // Ensure sortBy is set for JSP
        } else if ("lowToHigh".equals(sortBy)) {
            filteredItems.sort(Comparator.comparingDouble(FoodItem::getPrice));
        }

        // Paginate the sorted and filtered list
        List<FoodItem> foodItems = foodItemService.getPaginatedFoodItemsFromList(filteredItems, page, itemsPerPage);
        List<FoodItem> popularItems = foodItemService.getPopularItems(); // Get popular items (unfiltered for now)

        // Calculate total pages and items based on filtered list
        int totalItems = filteredItems.size(); // Use filtered list size
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // Set attributes for the JSP
        request.setAttribute("foodItems", foodItems);
        request.setAttribute("popularItems", popularItems);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("itemsPerPage", itemsPerPage);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("sortBy", sortBy); // Pass sortBy to JSP
        if (isReset) {
            request.setAttribute("reset", "true"); // Pass reset flag to JSP
        }

        // Forward to foods.jsp
        request.getRequestDispatcher("/foods.jsp").forward(request, response);
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
            FoodItem foodItem = foodItemService.getFoodItemById(foodId);
            if (foodItem == null) {
                throw new ServletException("Food item not found");
            }
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
}