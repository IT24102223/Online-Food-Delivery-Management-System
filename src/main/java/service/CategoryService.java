package service;

import java.model.Category;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static final String FILE_PATH = "categories.txt";

    // Create a new category
    public void createCategory(Category category) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(category.toString());
            writer.newLine();
        }
    }

    // Read all categories
    public List<Category> listCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    categories.add(new Category(parts[0], parts[1], parts[2]));
                }
            }
        }
        return categories;
    }

    // Update a category
    public void updateCategory(Category category) throws IOException {
        List<Category> categories = listCategories();
        boolean found = false;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryId().equals(category.getCategoryId())) {
                categories.set(i, category);
                found = true;
                break;
            }
        }
        if (found) {
            overwriteFile(categories);
        }
    }

    // Delete a category
    public void deleteCategory(String categoryId) throws IOException {
        List<Category> categories = listCategories();
        categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        overwriteFile(categories);
    }

    // Helper method to overwrite the file with updated list
    private void overwriteFile(List<Category> categories) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Category category : categories) {
                writer.write(category.toString());
                writer.newLine();
            }
        }
    }
}