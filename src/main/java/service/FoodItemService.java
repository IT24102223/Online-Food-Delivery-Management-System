package service;

import model.FoodItem;
import model.Category;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Comparator;
import java.util.stream.Collectors;

public class FoodItemService {
    private static final String FILE_PATH = "WEB-INF/resources/data/fooditems.txt";
    private static final Logger LOGGER = Logger.getLogger(FoodItemService.class.getName());
    private final ServletContext servletContext;
    private final CategoryService categoryService;

    public FoodItemService(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.categoryService = new CategoryService(servletContext);
    }

    // QuickSort implementation for sorting FoodItems by price
    private void quickSort(List<FoodItem> items, int low, int high, boolean ascending) {
        if (low < high) {
            int pi = partition(items, low, high, ascending);
            quickSort(items, low, pi - 1, ascending);
            quickSort(items, pi + 1, high, ascending);
        }
    }

    private int partition(List<FoodItem> items, int low, int high, boolean ascending) {
        double pivot = items.get(high).getPrice();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentPrice = items.get(j).getPrice();
            boolean swapCondition = ascending ? currentPrice <= pivot : currentPrice >= pivot;
            if (swapCondition) {
                i++;
                // Swap items[i] and items[j]
                FoodItem temp = items.get(i);
                items.set(i, items.get(j));
                items.set(j, temp);
            }
        }

        // Swap items[i+1] and items[high] (pivot)
        FoodItem temp = items.get(i + 1);
        items.set(i + 1, items.get(high));
        items.set(high, temp);

        return i + 1;
    }

    // Public method to sort food items by price using QuickSort
    public void sortFoodItemsByPrice(List<FoodItem> items, boolean ascending) {
        if (items == null || items.isEmpty()) {
            return;
        }
        quickSort(items, 0, items.size() - 1, ascending);
    }

    public void addFoodItem(FoodItem item) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        if (items.stream().anyMatch(existingItem -> existingItem.getFoodId().equals(item.getFoodId()))) {
            throw new IllegalArgumentException("Food ID " + item.getFoodId() + " already exists.");
        }
        items.add(item);
        try {
            saveAllFoodItems(items);
            LOGGER.info("Successfully added food item " + item.getFoodId() + " to fooditems.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save food item " + item.getFoodId() + " to fooditems.txt", e);
            throw e;
        }
    }

    public List<FoodItem> getAllFoodItems() throws IOException {
        List<FoodItem> items = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        LOGGER.info("Reading fooditems.txt from: " + realPath);
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("fooditems.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(FoodItem.fromCSV(line));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading fooditems.txt", e);
            throw e;
        }
        return items;
    }

    private void saveAllFoodItems(List<FoodItem> items) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        LOGGER.info("Writing to fooditems.txt at: " + realPath);
        File file = new File(realPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            LOGGER.info("Created parent directories for " + realPath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (FoodItem item : items) {
                writer.write(item.toCSV());
                writer.newLine();
            }
            LOGGER.info("Successfully wrote " + items.size() + " items to fooditems.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to fooditems.txt", e);
            throw e;
        }
    }

    public void updateFoodItem(FoodItem updatedItem) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        boolean updated = false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getFoodId().trim().equalsIgnoreCase(updatedItem.getFoodId().trim())) {
                items.set(i, updatedItem);
                updated = true;
                break;
            }
        }
        if (!updated) {
            LOGGER.warning("No food item found with ID " + updatedItem.getFoodId() + " for update");
            throw new IOException("Food item with ID " + updatedItem.getFoodId() + " not found");
        }
        saveAllFoodItems(items);
        LOGGER.info("Successfully updated food item " + updatedItem.getFoodId() + " in fooditems.txt");
    }

    public void deleteFoodItem(String foodId) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        items.removeIf(item -> item.getFoodId().equals(foodId));
        saveAllFoodItems(items);
    }

    public FoodItem getFoodItemById(String foodId) throws IOException {
        return getAllFoodItems().stream()
                .filter(item -> item.getFoodId().equals(foodId))
                .findFirst()
                .orElse(null);
    }

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

    public List<FoodItem> getPaginatedFoodItems(int page, int itemsPerPage) throws IOException {
        List<FoodItem> allItems = getAllFoodItems();
        return getPaginatedFoodItemsFromList(allItems, page, itemsPerPage);
    }

    public List<FoodItem> getPaginatedFoodItemsFromList(List<FoodItem> items, int page, int itemsPerPage) {
        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());
        if (start >= items.size()) {
            return new ArrayList<>();
        }
        return items.subList(start, end);
    }

    public int getTotalPages(int itemsPerPage) throws IOException {
        List<FoodItem> allItems = getAllFoodItems();
        return (int) Math.ceil((double) allItems.size() / itemsPerPage);
    }

    public List<FoodItem> filterFoodItems(List<FoodItem> items, String[] categoryIds, double maxPrice) throws IOException {
        List<FoodItem> filtered = new ArrayList<>(items);

        // Filter by category IDs if provided
        if (categoryIds != null && categoryIds.length > 0) {
            List<String> selectedCategoryIds = Arrays.asList(categoryIds);
            filtered = filtered.stream()
                    .filter(item -> selectedCategoryIds.contains(item.getCategoryId()))
                    .collect(Collectors.toList());
        }

        // Filter by price
        filtered = filtered.stream()
                .filter(item -> item.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        return filtered;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }
}