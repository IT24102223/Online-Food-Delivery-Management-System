<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>QuickBite</title>
    <link rel="icon" type="image/x-icon" href="external/letter-q.png">
    <!-- Google Fonts (DM Sans, Poppins, Patua One) -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One&display=swap">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css?v=1">
</head>
<body>
<%@ include file="header.jsp" %>
<!-- Video Section -->
<div class="container">
    <!-- Hero Section -->
    <div class="hero">
        <div class="hero-content">
            <h1>Craving Food? Get It Delivered Fast!</h1>
            <p>Order from top restaurants near you and enjoy fresh meals at your doorstep in minutes!</p>
            <div class="hero-buttons">
                <a href="${pageContext.request.contextPath}/food-items" class="find-food">Find Food</a>
            </div>
        </div>
    </div>

    <!-- Menu Section -->
    <div class="menu-section">
        <h2>Browse Our Menu</h2>
        <div class="menu-grid">
            <div class="menu-card">
                <div class="menu-icon"><i class="fas fa-coffee"></i></div>
                <h3>Breakfast</h3>
                <p>In the new era of technology we look in the future with certainty and pride for our life.</p>
                <a href="${pageContext.request.contextPath}/food-items" class="explore-menu">Explore Menu</a>
            </div>
            <div class="menu-card">
                <div class="menu-icon"><i class="fas fa-utensils"></i></div>
                <h3>Main Dishes</h3>
                <p>In the new era of technology we look in the future with certainty and pride for our life.</p>
                <a href="${pageContext.request.contextPath}/food-items" class="explore-menu">Explore Menu</a>
            </div>
            <div class="menu-card">
                <div class="menu-icon"><i class="fas fa-glass-whiskey"></i></div>
                <h3>Drinks</h3>
                <p>In the new era of technology we look in the future with certainty and pride for our life.</p>
                <a href="${pageContext.request.contextPath}/food-items" class="explore-menu">Explore Menu</a>
            </div>
            <div class="menu-card">
                <div class="menu-icon"><i class="fas fa-ice-cream"></i></div>
                <h3>Desserts</h3>
                <p>In the new era of technology we look in the future with certainty and pride for our life.</p>
                <a href="${pageContext.request.contextPath}/food-items" class="explore-menu">Explore Menu</a>
            </div>
        </div>
    </div>

    <!-- About Section -->
    <div class="about-section">
        <div class="about-content">
            <div class="about-text">
                <h2>Bringing Your Favorite Meals to Your Doorstep!</h2>
                <p>Why wait in line? Get delicious meals from top-rated restaurants delivered fast. Choose from a variety of cuisines, place your order, and enjoy hassle-free dining from the comfort of your home!</p>
                <a href="${pageContext.request.contextPath}/food-items" class="start-ordering">Start Ordering</a>
            </div>
            <div class="about-image">
                <img src="${pageContext.request.contextPath}/images/delivery.jpg" alt="Food delivery">
            </div>
        </div>
    </div>

    <!-- Features Section -->
    <div class="features-section">
        <h2>Lightning-Fast Delivery, Right to You!</h2>
        <p>Order now and get your food delivered in 30 minutes or less. We partner with top local restaurants to bring you the best flavors at unbeatable prices.</p>
        <div class="features-grid">
            <div class="feature">
                <i class="fas fa-clock"></i>
                <p>Delivery within 30 minutes</p>
            </div>
            <div class="feature">
                <i class="fas fa-tags"></i>
                <p>Best Offer & Prices</p>
            </div>
            <div class="feature">
                <i class="fas fa-shopping-cart"></i>
                <p>Online Services Available</p>
            </div>
        </div>
    </div>

    <!-- Testimonials Section -->
    <div class="testimonials-section">
        <h2>What Our Customers Say</h2>
        <div class="testimonials-grid">
            <div class="testimonial-card">
                <h3>"Fastest delivery ever!"</h3>
                <div class="rating">
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                </div>
                <p>I was surprised at how quickly my food arrived! The app is easy to use, and the meals are always hot and fresh. Definitely my go-to for ordering food!</p>
                <div class="user">
                    <div class="user-placeholder"><i class="fas fa-user"></i></div>
                    <div>
                        <p>Sophire Robson</p>
                        <p>Los Angeles, CA</p>
                    </div>
                </div>
            </div>
            <div class="testimonial-card">
                <h3>"Amazing variety & smooth experience!"</h3>
                <div class="rating">
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star-half-alt"></i>
                </div>
                <p>So many great restaurant options! I ordered from three different places, and everything came right on time. Love the tracking feature too!</p>
                <div class="user">
                    <div class="user-placeholder"><i class="fas fa-user"></i></div>
                    <div>
                        <p>Matt Cannon</p>
                        <p>San Diego, CA</p>
                    </div>
                </div>
            </div>
            <div class="testimonial-card">
                <h3>"Reliable and affordable"</h3>
                <div class="rating">
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="fas fa-star"></i>
                    <i class="far fa-star"></i>
                </div>
                <p>Finally, a food delivery service that doesn't cost a fortune! The deals and discounts make it even better. Highly recommended!</p>
                <div class="user">
                    <div class="user-placeholder"><i class="fas fa-user"></i></div>
                    <div>
                        <p>Andy Smith</p>
                        <p>San Francisco, CA</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>