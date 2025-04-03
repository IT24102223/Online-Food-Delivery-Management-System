<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Food Items</title>
</head>
<body>
<h1>Food Items</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Category</th>
        <th>Available</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${foodItems}" var="item">
        <tr>
            <td>${item.foodId}</td>
            <td>${item.name}</td>
            <td>${item.description}</td>
            <td>${item.price}</td>
            <td>${item.categoryId}</td>
            <td>${item.available ? 'Yes' : 'No'}</td>
            <td>
                <a href="food-items?action=edit&id=${item.foodId}">Edit</a>
                <a href="food-items?action=delete&id=${item.foodId}"
                   onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="food-items?action=new">Add New Food Item</a>
</body>
</html>