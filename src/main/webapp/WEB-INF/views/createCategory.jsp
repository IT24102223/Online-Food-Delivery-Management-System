<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Category</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-4">
        <h1 class="text-2xl font-bold mb-4">Create New Category</h1>
        <form action="createCategory" method="post" class="bg-white p-6 rounded shadow">
            <div class="mb-4">
                <label class="block text-gray-700">Category ID</label>
                <input type="text" name="categoryId" class="w-full p-2 border rounded" required>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700">Name</label>
                <input type="text" name="name" class="w-full p-2 border rounded" required>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700">Description</label>
                <textarea name="description" class="w-full p-2 border rounded" required></textarea>
            </div>
            <button type="submit" class="bg-blue-500 text-white p-2 rounded">Create</button>
        </form>
        <a href="listCategories" class="mt-4 inline-block text-blue-500">Back to List</a>
    </div>
</body>
</html>