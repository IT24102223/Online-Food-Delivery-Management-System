<%--
  Created by IntelliJ IDEA.
  User: acer
  Date: 5/20/2025
  Time: 6:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
</head>
<body>
<div class="header-container">
    <div class="header-inner">
        <!-- Top Row: Logo and User Actions -->
        <div class="top-row">
            <!-- Logo -->
            <div class="logo-container">
                <img alt="japanesefood8198" src="${pageContext.request.contextPath}/external/japanesefood8198-lw1q.svg">
                <a href="${pageContext.request.contextPath}/index.jsp">QuickBite</a>
            </div>
            <!-- User Actions -->
            <div class="login-signup">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- User is logged in -->
                        <button class="profile-btn" onclick="window.location.href='${pageContext.request.contextPath}/user?action=profile'">
                            <i class="fas fa-user"></i> Profile
                        </button>
                        <button class="logout-btn" onclick="window.location.href='${pageContext.request.contextPath}/user?action=logout'">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </button>
                    </c:when>
                    <c:otherwise>
                        <!-- User is not logged in -->
                        <button class="login-btn" onclick="window.location.href='${pageContext.request.contextPath}/login.jsp'">
                            <i class="fas fa-sign-in-alt"></i> Login
                        </button>
                        <button class="signup-btn" onclick="window.location.href='${pageContext.request.contextPath}/signup.jsp'">
                            <i class="fas fa-user-plus"></i> Signup
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- Bottom Row: Navigation Links -->
        <div class="nav-container">
            <!-- Hamburger Menu Toggle (Visible on Mobile) -->
            <div class="hamburger" onclick="toggleMenu()">
                <div></div>
                <div></div>
                <div></div>
            </div>
            <!-- Navigation Menu -->
            <div class="nav-menu" id="nav-menu">
                <a href="${pageContext.request.contextPath}/index.jsp" class="nav-link">Home</a>
                <a href="${pageContext.request.contextPath}/#foods" class="nav-link">Foods</a>
                <a href="${pageContext.request.contextPath}/myorders" class="nav-link">My Orders</a>
                <a href="${pageContext.request.contextPath}/contact.jsp" class="nav-link">Contact Us</a>
                <a href="${pageContext.request.contextPath}/about.jsp" class="nav-link">About Us</a>
            </div>
        </div>
    </div>
    <!-- Cart Button -->
    <div class="cart-button">
        <a href="${pageContext.request.contextPath}/cart">
            <i class="fas fa-shopping-cart"></i>
        </a>
    </div>
</div>

<!-- Inline JavaScript for Hamburger Menu -->
<script>
    function toggleMenu() {
        var menu = document.getElementById('nav-menu');
        menu.classList.toggle('active');
    }
</script>
</body>
</html>