package service;

import model.Category;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {
    private static final String FILE_PATH = "WEB-INF/resources/data/categories.txt";
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
    private final ServletContext servletContext;

    public CategoryService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    // Get all categories from the file
    public List<Category> getAllCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            LOGGER.warning("categories.txt does not exist at " + realPath + ". Creating new file.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return categories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(Category.fromCSV(line));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading categories.txt", e);
            throw e;
        }
        return categories;
    }

    // Get category by ID
    public Category getCategoryById(String categoryId) throws IOException {
        return getAllCategories().stream()
                .filter(category -> category.getCategoryId().equals(categoryId))
                .findFirst()
                .orElse(null);
    }

    // Add a category to the file
    public void addCategory(Category category) throws IOException {
        List<Category> categories = getAllCategories();
        if (categories.stream().anyMatch(existing -> existing.getCategoryId().equals(category.getCategoryId()))) {
            throw new IllegalArgumentException("Category ID " + category.getCategoryId() + " already exists.");
        }
        categories.add(category);
        try {
            saveAllCategories(categories);
            LOGGER.info("Successfully added category " + category.getCategoryId() + " to categories.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save category " + category.getCategoryId() + " to categories.txt", e);
            throw e;
        }
    }

    // Update an existing category
    public void updateCategory(Category updatedCategory) throws IOException {
        List<Category> categories = getAllCategories();
        boolean updated = false;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryId().equals(updatedCategory.getCategoryId())) {
                categories.set(i, updatedCategory);
                updated = true;
                break;
            }
        }
        if (!updated) {
            LOGGER.warning("No category found with ID " + updatedCategory.getCategoryId() + " for update");
            throw new IOException("Category with ID " + updatedCategory.getCategoryId() + " not found");
        }
        saveAllCategories(categories);
        LOGGER.info("Successfully updated category " + updatedCategory.getCategoryId() + " in categories.txt");
    }

    // Delete a category by ID
    public void deleteCategory(String categoryId) throws IOException {
        List<Category> categories = getAllCategories();
        categories.removeIf(category -> category.getCategoryId().equals(categoryId));
        saveAllCategories(categories);
        LOGGER.info("Successfully deleted category " + categoryId + " from categories.txt");
    }

    // Save all categories to the file
    private void saveAllCategories(List<Category> categories) throws IOException {
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            LOGGER.severe("Unable to resolve real path for " + FILE_PATH);
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        LOGGER.info("Writing to categories.txt at: " + realPath);
        File file = new File(realPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            LOGGER.info("Created parent directories for " + realPath);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Category category : categories) {
                writer.write(category.toCSV());
                writer.newLine();
            }
            LOGGER.info("Successfully wrote " + categories.size() + " categories to categories.txt");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to categories.txt", e);
            throw e;
        }
    }
}