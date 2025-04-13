package service;

import model.Category;
import jakarta.servlet.ServletContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static final String FILE_PATH = "WEB-INF/resources/data/categories.txt";
    private final ServletContext servletContext;

    public CategoryService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    // Get all categories from the file
    public List<Category> getAllCategories() throws IOException {
        List<Category> categories = new ArrayList<>();
        String realPath = servletContext.getRealPath(FILE_PATH);
        if (realPath == null) {
            throw new IOException("Unable to resolve real path for " + FILE_PATH);
        }
        File file = new File(realPath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            return categories; // Return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(Category.fromCSV(line));
            }
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
}