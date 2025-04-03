<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Food Item</title>
    <style>
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 120px; }
    </style>
</head>
<body>
<h1>Edit Food Item</h1>
<form action="food-items" method="post">
    <input type="hidden" name="action" value="update">

    <div class="form-group">
        <label for="foodId">Food ID:</label>
        <input type="text" id="foodId" name="foodId"
               value="${foodItem.foodId}" readonly>
    </div>

    <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name"
               value="${foodItem.name}" required>
    </div>

    <div class="form-group">
        <label for="description">Description:</label>
        <textarea id="description" name="description" rows="3">${foodItem.description}</textarea>
    </div>

    <div class="form-group">
        <label for="price">Price:</label>
        <input type="number" id="price" name="price" step="0.01" min="0"
               value="${foodItem.price}" required>
    </div>

    <div class="form-group">
        <label for="categoryId">Category ID:</label>
        <input type="text" id="categoryId" name="categoryId"
               value="${foodItem.categoryId}" required>
    </div>

    <div class="form-group">
        <label for="availability">Available:</label>
        <input type="checkbox" id="availability" name="availability"
               value="true" ${foodItem.available ? 'checked' : ''}>
    </div>

    <div class="form-group">
        <input type="submit" value="Update
