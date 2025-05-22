<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About QuickBite</title>
    <link rel="icon" type="image/x-icon" href="external/letter-q.png">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<!-- Hero Section -->
<section class="about-hero fadeIn">
    <div class="about-content">
        <div class="hero-text">
            <h1>Effortless Food Delivery Management for Your Business</h1>
            <p>Our platform was built to revolutionize food delivery by merging technology with seamless logistics. Whether you're a restaurant, cloud kitchen, or food entrepreneur, we help streamline your deliveries, optimize efficiency, and enhance customer satisfaction.</p>
        </div>
        <div class="hero-image">
            <img src="${pageContext.request.contextPath}/external/pexelscottonbrostudio425213910847-9tvm-900h.png" alt="Food Delivery">
        </div>
    </div>
</section>

<!-- Features Section -->
<section class="about-features fadeIn">
    <h2>Optimize Your Food Deliveries with Smart Technology</h2>
    <div class="features-grid">
        <div class="feature-card">
            <i class="fas fa-utensils"></i>
            <h3>Multi-Restaurant Integration</h3>
            <p>Connect multiple restaurant partners on a single platform.</p>
        </div>
        <div class="feature-card">
            <i class="fas fa-map-marker-alt"></i>
            <h3>Real-Time Order Tracking</h3>
            <p>Keep track of every order from kitchen to doorstep.</p>
        </div>
        <div class="feature-card">
            <i class="fas fa-route"></i>
            <h3>AI-Powered Route Optimization</h3>
            <p>Ensure the fastest and most cost-effective deliveries.</p>
        </div>
    </div>
</section>

<!-- Facts Section -->
<section class="about-facts fadeIn">
    <div class="facts-content">
        <div class="facts-text">
            <h2>A Little Information for Our Valuable Guests</h2>
            <p>At QuickBite, we believe that dining is not just about food, but also about the overall experience. Our staff, renowned for their warmth and dedication, strives to make every visit an unforgettable event.</p>
        </div>
        <div class="facts-grid">
            <div class="fact-card">
                <h3>1000+</h3>
                <p>Partnered Restaurants</p>
            </div>
            <div class="fact-card">
                <h3>2018</h3>
                <p>Established</p>
            </div>
            <div class="fact-card">
                <h3>500K+</h3>
                <p>Orders Processed</p>
            </div>
            <div class="fact-card">
                <h3>99%</h3>
                <p>On-Time Deliveries</p>
            </div>
        </div>
    </div>
</section>

<!-- Testimonials Section -->
<section class="about-testimonials fadeIn">
    <h2>What Our Customers Say</h2>
    <div class="testimonials-grid">
        <div class="testimonial-card">
            <p class="quote">"Game changer for our restaurant!"</p>
            <p>Since using this platform, our delivery times have improved, and we've seen a 40% boost in customer retention.</p>
            <div class="user-info">
                <img src="${pageContext.request.contextPath}/external/ellipse190109-zqeu-200h.png" alt="Sophire Robson">
                <div>
                    <p>Sophire Robson</p>
                    <p>Los Angeles, CA</p>
                </div>
            </div>
        </div>
        <div class="testimonial-card">
            <p class="quote">"Simply seamless"</p>
            <p>QuickBite exceeded my expectations. The ambiance was cozy, and each dish was beautifully presented.</p>
            <div class="user-info">
                <img src="${pageContext.request.contextPath}/external/image0110-k04g-200h.png" alt="Matt Cannon">
                <div>
                    <p>Matt Cannon</p>
                    <p>San Diego, CA</p>
                </div>
            </div>
        </div>
        <div class="testimonial-card">
            <p class="quote">"Best delivery platform"</p>
            <p>Our drivers are always on time, and our customers love the real-time tracking feature.</p>
            <div class="user-info">
                <img src="${pageContext.request.contextPath}/external/image0111-auzh-200h.png" alt="Andy Smith">
                <div>
                    <p>Andy Smith</p>
                    <p>San Francisco, CA</p>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="footer.jsp" %>

<script>
    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            document.querySelector(this.getAttribute('href')).scrollIntoView({
                behavior: 'smooth'
            });
        });
    });

    // Pulse animation on feature card hover
    document.querySelectorAll('.feature-card, .testimonial-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.classList.add('pulse');
        });
        card.addEventListener('animationend', function() {
            this.classList.remove('pulse');
        });
    });

    // Toast notification example (can be triggered by backend)
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const toast = document.getElementById('toast');
        if (urlParams.get('success') === 'true') {
            toast.textContent = "Welcome to QuickBite!";
            toast.classList.add('show');
            setTimeout(() => {
                toast.classList.remove('show');
            }, 2000);
        }
    };
</script>
</body>
</html>