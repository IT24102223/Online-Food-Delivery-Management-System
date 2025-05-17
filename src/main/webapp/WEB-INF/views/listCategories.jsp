<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fooddelivery.model.Category" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Category List</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-4">
        <h1 class="text-2xl font-bold mb-4">Category Management</h1>
        <a href="createCategory.jsp" class="bg-green-500 text-white p-2 rounded mb-4 inline-block">Add Category</a>
        <table class="w-full bg-white shadow rounded">
            <thead>
                <tr class="bg-gray-200">
                    <th class="p-2">ID</th>
                    <th class="p-2">Name</th>
                    <th class="p-2">Description</th>
                    <th class="p-2">Actions</th>
                </tr>
            </thead>
            <tbody>
                <% List<Category> categories = (List<Category>) request.getAttribute("categories");
                   if (categories != null) {
                       for (Category category : categories) { %>
                <tr>
                    <td class="p-2"><%= category.getCategoryId() %></td>
                    <td class="p-2"><%= category.getName() %></td>
                    <td class="p-2"><%= category.getDescription() %></td>
                    <td class="p-2">
                        <a href="updateCategory.jsp?categoryId=<%= category.getCategoryId() %>" class="text-blue-500">Edit</a>
                        <a href="deleteCategory?categoryId=<%= category.getCategoryId() %>" class="text-red-500 ml-2" onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </tr>
                <% }
                   } %>
            </tbody>
        </table>
    </div>
</body>
</html>