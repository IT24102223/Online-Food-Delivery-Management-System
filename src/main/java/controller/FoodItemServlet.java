package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FoodItem;
import model.Category;
import service.FoodItemService;
import service.CategoryService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/food-items", "/admin/food-items"})
public class FoodItemServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(FoodItemServlet.class.getName());
    private FoodItemService foodItemService;

    @Override
    public void init() throws ServletException {
        super.init();
        foodItemService = new FoodItemService(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String action = request.getParameter("action");
        if ("/admin/food-items".equals(servletPath)) {
            if ("edit".equals(action)) {
                showEditForm(request, response);
            } else if ("delete".equals(action)) {
                deleteFoodItem(request, response);
            } else if ("deleteCategory".equals(action)) {
                deleteCategory(request, response);
            } else {
                listFoodItems(request, response, servletPath);
            }
        } else {
            listFoodItems(request, response, servletPath);
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
        } else if ("addCategory".equals(action)) {
            addCategory(request, response);
        } else if ("updateCategory".equals(action)) {
            updateCategory(request, response);
        } else {
            listFoodItems(request, response, request.getServletPath());
        }
    }

    private void listFoodItems(HttpServletRequest request, HttpServletResponse response, String servletPath)
            throws ServletException, IOException {
        try {
            // Get pagination parameters, default to page 1 if invalid or not provided
            int page;
            try {
                page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
            } catch (NumberFormatException e) {
                page = 1; // Default to page 1 on invalid input
            }
            int itemsPerPage;
            String itemsPerPageParam = request.getParameter("itemsPerPage");
            try {
                itemsPerPage = (itemsPerPageParam != null && !itemsPerPageParam.trim().isEmpty())
                        ? Integer.parseInt(itemsPerPageParam)
                        : ("/admin/food-items".equals(servletPath) ? 10 : 8);
            } catch (NumberFormatException e) {
                itemsPerPage = "/admin/food-items".equals(servletPath) ? 10 : 8;
            }

            // Check for reset parameter
            boolean isReset = "true".equals(request.getParameter("reset"));
            String[] selectedCategories = isReset ? null : request.getParameterValues("categories"); // Clear categories if reset
            String priceRangeStr = isReset ? "500" : request.getParameter("priceRange"); // Default to 500 if reset
            String sortBy = isReset ? "foodId" : request.getParameter("sortBy"); // Default to foodId for admin, highToLow for foods.jsp

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

            // Apply sorting based on sortBy parameter
            if (sortBy == null || sortBy.isEmpty()) {
                if ("/admin/food-items".equals(servletPath)) {
                    filteredItems.sort(Comparator.comparing(FoodItem::getFoodId)); // Default sort by foodId for admin
                } else {
                    foodItemService.sortFoodItemsByPrice(filteredItems, false); // Default highToLow for foods.jsp using QuickSort
                }
            } else if ("highToLow".equals(sortBy)) {
                foodItemService.sortFoodItemsByPrice(filteredItems, false); // QuickSort descending
            } else if ("lowToHigh".equals(sortBy)) {
                foodItemService.sortFoodItemsByPrice(filteredItems, true); // QuickSort ascending
            } else if ("foodId".equals(sortBy)) {
                filteredItems.sort(Comparator.comparing(FoodItem::getFoodId)); // Keep Comparator for foodId
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
            request.setAttribute("categories", foodItemService.getCategoryService().getAllCategories());
            if (isReset) {
                request.setAttribute("reset", "true"); // Pass reset flag to JSP
            }

            if ("/admin/food-items".equals(servletPath)) {
                LOGGER.info("Forwarding to adminDashboard.jsp");
                request.getRequestDispatcher("/WEB-INF/views/food/adminDashboard.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/foods.jsp").forward(request, response);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error listing food items: " + e.getMessage(), e);
            throw new ServletException("Unable to list food items", e);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodId = request.getParameter("id");
        try {
            FoodItem foodItem = foodItemService.getFoodItemById(foodId);
            if (foodItem == null) {
                LOGGER.warning("Food item not found: " + foodId);
                request.setAttribute("error", "Food item not found");
            } else {
                request.setAttribute("foodItem", foodItem);
            }
            request.setAttribute("categories", foodItemService.getCategoryService().getAllCategories());
            LOGGER.info("Forwarding to adminDashboard.jsp for edit form");
            request.getRequestDispatcher("/WEB-INF/views/food/adminDashboard.jsp").forward(request, response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving food item: " + e.getMessage(), e);
            throw new ServletException("Error retrieving food item", e);
        }
    }

    private void addFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String foodId = request.getParameter("foodId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String categoryId = request.getParameter("categoryId");
            String availability = request.getParameter("availability");

            if (foodId == null || foodId.trim().isEmpty()) {
                throw new IllegalArgumentException("Food ID is required.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name is required.");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Price is required.");
            }
            if (categoryId == null || categoryId.trim().isEmpty()) {
                throw new IllegalArgumentException("Category is required.");
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    throw new IllegalArgumentException("Price cannot be negative.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid price format.");
            }

            Category category = foodItemService.getCategoryService().getCategoryById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Invalid category ID: " + categoryId);
            }

            FoodItem newItem = new FoodItem(
                    foodId,
                    name,
                    description != null ? description : "",
                    price,
                    categoryId,
                    "true".equals(availability)
            );
            foodItemService.addFoodItem(newItem);
            request.setAttribute("success", "Food item added successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error adding food item: " + e.getMessage(), e);
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add food item: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to add food item due to server error.");
            showEditForm(request, response);
        }
    }

    private void updateFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String foodId = request.getParameter("foodId");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String categoryId = request.getParameter("categoryId");
            String availability = request.getParameter("availability");

            if (foodId == null || foodId.trim().isEmpty()) {
                throw new IllegalArgumentException("Food ID is required.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name is required.");
            }
            if (priceStr == null || priceStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Price is required.");
            }
            if (categoryId == null || categoryId.trim().isEmpty()) {
                throw new IllegalArgumentException("Category is required.");
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    throw new IllegalArgumentException("Price cannot be negative.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid price format.");
            }

            Category category = foodItemService.getCategoryService().getCategoryById(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Invalid category ID: " + categoryId);
            }

            FoodItem updatedItem = new FoodItem(
                    foodId,
                    name,
                    description != null ? description : "",
                    price,
                    categoryId,
                    "true".equals(availability)
            );
            foodItemService.updateFoodItem(updatedItem);
            request.setAttribute("success", "Food item updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error updating food item: " + e.getMessage(), e);
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update food item: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to update food item due to server error.");
            showEditForm(request, response);
        }
    }

    private void deleteFoodItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String foodId = request.getParameter("id");
        try {
            foodItemService.deleteFoodItem(foodId);
            request.setAttribute("success", "Food item deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete food item: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to delete food item due to server error.");
            listFoodItems(request, response, "/admin/food-items");
        }
    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String categoryId = request.getParameter("categoryId");
            String name = request.getParameter("categoryName");
            String description = request.getParameter("categoryDescription");

            if (categoryId == null || categoryId.trim().isEmpty()) {
                throw new IllegalArgumentException("Category ID is required.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name is required.");
            }

            Category newCategory = new Category(
                    categoryId,
                    name,
                    description != null ? description : ""
            );
            foodItemService.getCategoryService().addCategory(newCategory);
            request.setAttribute("success", "Category added successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error adding category: " + e.getMessage(), e);
            request.setAttribute("error", e.getMessage());
            listFoodItems(request, response, "/admin/food-items");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to add category: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to add category due to server error.");
            listFoodItems(request, response, "/admin/food-items");
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String categoryId = request.getParameter("categoryId");
            String name = request.getParameter("categoryName");
            String description = request.getParameter("categoryDescription");

            if (categoryId == null || categoryId.trim().isEmpty()) {
                throw new IllegalArgumentException("Category ID is required.");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name is required.");
            }

            Category updatedCategory = new Category(
                    categoryId,
                    name,
                    description != null ? description : ""
            );
            foodItemService.getCategoryService().updateCategory(updatedCategory);
            request.setAttribute("success", "Category updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error updating category: " + e.getMessage(), e);
            request.setAttribute("error", e.getMessage());
            listFoodItems(request, response, "/admin/food-items");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to update category: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to update category due to server error.");
            listFoodItems(request, response, "/admin/food-items");
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryId = request.getParameter("id");
        try {
            // Check if any food items are associated with this category
            List<FoodItem> allItems = foodItemService.getAllFoodItems();
            boolean hasAssociatedItems = allItems.stream()
                    .anyMatch(item -> item.getCategoryId().equals(categoryId));
            if (hasAssociatedItems) {
                throw new IllegalArgumentException("Cannot delete category with associated food items.");
            }
            foodItemService.getCategoryService().deleteCategory(categoryId);
            request.setAttribute("success", "Category deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/food-items");
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Validation error deleting category: " + e.getMessage(), e);
            request.setAttribute("error", e.getMessage());
            listFoodItems(request, response, "/admin/food-items");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete category: " + e.getMessage(), e);
            request.setAttribute("error", "Failed to delete category due to server error.");
            listFoodItems(request, response, "/admin/food-items");
        }
    }
}