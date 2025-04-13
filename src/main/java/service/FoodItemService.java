package service;

import model.FoodItem;
import model.Category;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class FoodItemService {
    private static final String FILE_PATH = "WEB-INF/resources/data/fooditems.txt";
    private final ServletContext servletContext;
    private final CategoryService categoryService; // Added to support category filtering

    public FoodItemService(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.categoryService = new CategoryService(servletContext); // Initialize CategoryService
    }

    // Add a food item to the file
    public void addFoodItem(FoodItem item) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        items.add(item);
        saveAllFoodItems(items);
    }

    // Get all food items from the file
    public List<FoodItem> getAllFoodItems() throws IOException {
        List<FoodItem> items = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(FoodItem.fromCSV(line));
            }
        }
        return items;
    }

    // Save all food items to the file
    private void saveAllFoodItems(List<FoodItem> items) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(realPath))) {
            for (FoodItem item : items) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        }
    }

    // Update an existing food item
    public void updateFoodItem(FoodItem updatedItem) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getFoodId().equals(updatedItem.getFoodId())) {
                items.set(i, updatedItem);
                break;
            }
        }
        saveAllFoodItems(items);
    }

    // Delete a food item by ID
    public void deleteFoodItem(String foodId) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        items.removeIf(item -> item.getFoodId().equals(foodId));
        saveAllFoodItems(items);
    }

    // Get food item by ID
    public FoodItem getFoodItemById(String foodId) throws IOException {
        return getAllFoodItems().stream()
                .filter(item -> item.getFoodId().equals(foodId))
                .findFirst()
                .orElse(null);
    }

    // Get popular food items (top 6 most ordered)
    public List<FoodItem> getPopularItems() throws IOException {
        List<FoodItem> allItems = getAllFoodItems();
        if (allItems == null || allItems.isEmpty()) {
            return new ArrayList<>();
        }
        return allItems.stream()
                .sorted(Comparator.comparingInt(FoodItem::getOrderCount).reversed())
                .limit(6)
                .collect(Collectors.toList());
    }

    // Increment order count for a food item
    public void incrementOrderCount(String foodId) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        boolean found = false;
        for (FoodItem item : items) {
            if (item.getFoodId().equals(foodId)) {
                item.setOrderCount(item.getOrderCount() + 1);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IOException("Food item with ID " + foodId + " not found");
        }
        saveAllFoodItems(items);
    }

    // Get paginated food items from all items
    public List<FoodItem> getPaginatedFoodItems(int page, int itemsPerPage) throws IOException {
        List<FoodItem> allItems = getAllFoodItems();
        return getPaginatedFoodItemsFromList(allItems, page, itemsPerPage);
    }

    // Get paginated food items from a specific list
    public List<FoodItem> getPaginatedFoodItemsFromList(List<FoodItem> items, int page, int itemsPerPage) {
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());
        if (start >= items.size()) {
            return new ArrayList<>();
        }
        return items.subList(start, end);
    }

    // Get total number of pages for all items
    public int getTotalPages(int itemsPerPage) throws IOException {
        List<FoodItem> allItems = getAllFoodItems();
        return (int) Math.ceil((double) allItems.size() / itemsPerPage);
    }

    // Filter food items by categories and price using CategoryService
    public List<FoodItem> filterFoodItems(List<FoodItem> items, String[] categoryNames, double maxPrice) throws IOException {
        List<FoodItem> filtered = new ArrayList<>(items);

        // Filter by categories if provided
        if (categoryNames != null && categoryNames.length > 0) {
            List<String> selectedCategoryNames = Arrays.asList(categoryNames);
            List<Category> allCategories = categoryService.getAllCategories(); // Fetch all categories dynamically
            filtered = filtered.stream()
                    .filter(item -> {
                        Category category = allCategories.stream()
                                .filter(c -> c.getCategoryId().equals(item.getCategoryId()))
                                .findFirst()
                                .orElse(null);
                        return category != null && selectedCategoryNames.contains(category.getName());
                    })
                    .collect(Collectors.toList());
        }

        // Filter by price
        filtered = filtered.stream()
                .filter(item -> item.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        return filtered;
    }
}