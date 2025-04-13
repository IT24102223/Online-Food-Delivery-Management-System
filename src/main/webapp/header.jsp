<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/header.css">
</head>
<body>
<div class="header-container">
    <div class="header-inner">
        <!-- Top Row: Logo and Login/Signup Buttons -->
        <div class="top-row">
            <!-- Logo -->
            <div class="logo-container">
                <img alt="japanesefood8198" src="external/japanesefood8198-lw1q.svg">
                <a href="#home">QuickBite</a>
            </div>
            <!-- Login/Signup Buttons -->
            <div class="login-signup">
                <button class="login-btn" onclick="window.location.href='/login'">
                    <i class="fas fa-sign-in-alt"></i> Login
                </button>
                <button class="signup-btn" onclick="window.location.href='/signup'">
                    <i class="fas fa-user-plus"></i> Signup
                </button>
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
                <a href="#home" class="nav-link">Home</a>
                <a href="#about" class="nav-link">About</a>
                <a href="#foods" class="nav-link">Foods</a>
                <a href="/order" class="nav-link">Order Now</a>
                <a href="#contact" class="nav-link">Contact</a>
            </div>
        </div>
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