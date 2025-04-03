package model;

public class Category {
    private String categoryId;
    private String name;
    private String description;

    public Category(String categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    // Getters & Setters
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public String toCSV() {
        return String.format("%s,%s,%s", categoryId, name, description);
    }

    public static Category fromCSV(String csvLine) {
        String[] data = csvLine.split(",");
        return new Category(data[0], data[1], data[2]);
    }
}