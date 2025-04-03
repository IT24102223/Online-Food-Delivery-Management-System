<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Online Food Delivery System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://source.unsplash.com/random/1200x600/?food');
            background-size: cover;
            color: white;
            padding: 100px 20px;
            text-align: center;
        }
        .feature-card {
            transition: transform 0.3s;
        }
        .feature-card:hover {
            transform: translateY(-10px);
        }
    </style>
</head>
<body>
<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="#">FoodExpress</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="food-items">Menu</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Orders</a>
                </li>
            </ul>
            <div class="d-flex">
                <a href="login.jsp" class="btn btn-outline-light me-2">Login</a>
                <a href="register.jsp" class="btn btn-primary">Register</a>
            </div>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<section class="hero-section mb-5">
    <div class="container">
        <h1 class="display-4 fw-bold">Delicious Food Delivered To Your Doorstep</h1>
        <p class="lead">Order from your favorite restaurants with just a few clicks</p>
        <a href="food-items" class="btn btn-primary btn-lg">Order Now</a>
    </div>
</section>

<!-- Features Section -->
<div class="container mb-5">
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card feature-card h-100">
                <div class="card-body text-center">
                    <i class="bi bi-truck fs-1 text-primary mb-3"></i>
                    <h5 class="card-title">Fast Delivery</h5>
                    <p class="card-text">Get your food delivered in under 30 minutes or it's free!</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card feature-card h-100">
                <div class="card-body text-center">
                    <i class="bi bi-currency-dollar fs-1 text-primary mb-3"></i>
                    <h5 class="card-title">Best Prices</h5>
                    <p class="card-text">Enjoy great discounts and offers every day.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card feature-card h-100">
                <div class="card-body text-center">
                    <i class="bi bi-shield-check fs-1 text-primary mb-3"></i>
                    <h5 class="card-title">Food Safety</h5>
                    <p class="card-text">100% hygiene and quality checked food.</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Popular Items (Dynamic with JSTL) -->
<div class="container mb-5">
    <h2 class="text-center mb-4">Popular Items</h2>
    <div class="row g-4">
        <c:forEach var="item" items="${popularItems}" begin="0" end="5">
            <div class="col-md-4 col-lg-2">
                <div class="card h-100">
                    <img src="${item.imageUrl}" class="card-img-top" alt="${item.name}">
                    <div class="card-body">
                        <h6 class="card-title">${item.name}</h6>
                        <p class="card-text text-danger fw-bold">$${item.price}</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="text-center mt-4">
        <a href="food-items" class="btn btn-outline-primary">View Full Menu</a>
    </div>
</div>

<!-- Footer -->
<footer class="bg-dark text-white py-4">
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <h5>FoodExpress</h5>
                <p>Delivering happiness since 2023</p>
            </div>
            <div class="col-md-3">
                <h5>Links</h5>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-white">About Us</a></li>
                    <li><a href="#" class="text-white">Contact</a></li>
                    <li><a href="#" class="text-white">Privacy Policy</a></li>
                </ul>
            </div>
            <div class="col-md-3">
                <h5>Connect</h5>
                <a href="#" class="text-white me-2"><i class="bi bi-facebook"></i></a>
                <a href="#" class="text-white me-2"><i class="bi bi-twitter"></i></a>
                <a href="#" class="text-white me-2"><i class="bi bi-instagram"></i></a>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap JS and Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</body>
</html>