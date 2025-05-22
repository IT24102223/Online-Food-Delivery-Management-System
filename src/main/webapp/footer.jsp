<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"> <!-- Added Font Awesome -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
</head>
<body>
<div class="footer-container">
    <img alt="BG0954" src="${pageContext.request.contextPath}/external/bg0954-bvog-700h.png" class="footer-bg">
    <div class="footer-content">
        <!-- Bottom Section -->
        <div class="footer-bottom">
            <span class="footer-copyright">Copyright © 2025 QuickBite. All Rights Reserved.</span>
            <img alt="Line3110958" src="${pageContext.request.contextPath}/external/line3110958-sh2.svg" class="footer-divider">
        </div>
        <!-- Top Section -->
        <div class="footer-top">
            <!-- Column 1 -->
            <div class="footer-col-1">
                <div class="footer-logo-container">
                    <img alt="japanesefood1013" src="${pageContext.request.contextPath}/external/japanesefood1013-di4.svg" class="footer-logo-img">
                    <div class="footer-logo-text-container">
                        <span class="footer-logo-text">QuickBite</span>
                    </div>
                </div>
                <span class="footer-description">We connect you with the best restaurants in town, delivering your favorite meals hot and fresh. Fast, reliable, and affordable—just the way you like it!</span>
                <!-- Social Icons -->
                <div class="footer-social">
                    <div class="footer-social-icon">
                        <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" aria-label="Follow us on Twitter">
                            <i class="fab fa-twitter"></i>
                        </a>
                    </div>
                    <div class="footer-social-icon">
                        <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" aria-label="Follow us on Facebook">
                            <i class="fab fa-facebook-f"></i>
                        </a>
                    </div>
                    <div class="footer-social-icon">
                        <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" aria-label="Follow us on Instagram">
                            <i class="fab fa-instagram"></i>
                        </a>
                    </div>
                    <div class="footer-social-icon">
                        <a href="https://github.com" target="_blank" rel="noopener noreferrer" aria-label="Follow us on GitHub">
                            <i class="fab fa-github"></i>
                        </a>
                    </div>
                </div>
            </div>
            <!-- Column 2 -->
            <div class="footer-col-2">
                <div class="footer-col-2-section">
                    <span class="footer-col-2-heading">Pages</span>
                    <div class="footer-col-2-links">
                        <a href="${pageContext.request.contextPath}/index.jsp" class="footer-col-2-link">Home</a>
                        <a href="${pageContext.request.contextPath}/about.jsp" class="footer-col-2-link">About</a>
                        <a href="${pageContext.request.contextPath}/food-items" class="footer-col-2-link">Foods</a>
                        <a href="#pricing" class="footer-col-2-link">Pricing</a>
                        <a href="${pageContext.request.contextPath}/contact.jsp" class="footer-col-2-link">Contact</a>
                        <a href="#delivery" class="footer-col-2-link">Delivery</a>
                    </div>
                </div>
                <div class="footer-col-2-section">
                    <span class="footer-col-2-heading">Utility Pages</span>
                    <div class="footer-col-2-links">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="footer-col-2-link">Log In</a>
                        <a href="#password-protected" class="footer-col-2-link">Password Protected</a>
                        <a href="#changelog" class="footer-col-2-link">Changelog</a>
                        <a href="#more" class="footer-col-2-link">View More</a>
                    </div>
                </div>
            </div>
            <!-- Column 3 -->
            <div class="footer-col-3">
                <span class="footer-col-3-heading">Follow Us On Instagram</span>
                <div class="footer-gallery">
                    <div class="footer-gallery-img-container footer-gallery-img-1">
                        <img alt="pexelssteve378988510101" src="${pageContext.request.contextPath}/external/pexelssteve378988510101-hgjg-300w.png">
                    </div>
                    <div class="footer-gallery-img-container footer-gallery-img-2">
                        <img alt="eilivacerond5pbkqj0lu8unsplash10101" src="${pageContext.request.contextPath}/external/eilivacerond5pbkqj0lu8unsplash10101-orfw-200h.png">
                    </div>
                    <div class="footer-gallery-img-container footer-gallery-img-3">
                        <img alt="pexelsellaolsson164077210101" src="${pageContext.request.contextPath}/external/pexelsellaolsson164077210101-n8wx-300w.png">
                    </div>
                    <div class="footer-gallery-img-container footer-gallery-img-4">
                        <img alt="pexelsash37646410102" src="${pageContext.request.contextPath}/external/pexelsash37646410102-l6or-300h.png">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>