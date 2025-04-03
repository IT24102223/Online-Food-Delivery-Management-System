package service;

import model.FoodItem;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FoodItemService {
    private static final String FILE_PATH = "../webapp/WEB-INF/resources/data/fooditems.txt";

    // Add a food item to the file
    public void addFoodItem(FoodItem item) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        items.add(item);
        saveAllFoodItems(items);
    }

    // Get all food items from the file
    public List<FoodItem> getAllFoodItems() throws IOException {
        List<FoodItem> items = new ArrayList<>();
        File file = new File(FILE_PATH);

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
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
        return getAllFoodItems().stream()
                .sorted(Comparator.comparingInt(FoodItem::getOrderCount).reversed())
                .limit(6)
                .collect(Collectors.toList());
    }

    // Increment order count for a food item
    public void incrementOrderCount(String foodId) throws IOException {
        List<FoodItem> items = getAllFoodItems();
        for (FoodItem item : items) {
            if (item.getFoodId().equals(foodId)) {
                item.setOrderCount(item.getOrderCount() + 1);
                break;
            }
        }
        saveAllFoodItems(items);
    }
}