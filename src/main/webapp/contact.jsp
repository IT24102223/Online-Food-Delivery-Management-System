<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Contact QuickBite</title>
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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contact.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<!-- Hero Section -->
<section class="contact-hero fadeIn">
  <div class="hero-content">
    <h1>Contact Us</h1>
    <p>We consider all the drivers of change to give you the components you need to create a truly remarkable experience.</p>
  </div>
</section>

<!-- Contact Form and Info Section -->
<section class="contact-main fadeIn">
  <div class="contact-container">
    <!-- Contact Form -->
    <div class="contact-form">
      <form id="contactForm" action="${pageContext.request.contextPath}/contact" method="POST">
        <div class="form-row">
          <div class="form-group">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" placeholder="Your Name" required>
          </div>
          <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" placeholder="Your Email" required>
          </div>
        </div>
        <div class="form-row">
          <div class="form-group full-width">
            <label for="subject">Subject</label>
            <input type="text" id="subject" name="subject" placeholder="Subject" required>
          </div>
        </div>
        <div class="form-row">
          <div class="form-group full-width">
            <label for="message">Message</label>
            <textarea id="message" name="message" placeholder="Your Message" rows="5" required></textarea>
          </div>
        </div>
        <button type="submit" class="submit-btn"><i class="fas fa-paper-plane"></i> Send</button>
      </form>
    </div>
    <!-- Contact Info -->
    <div class="contact-info">
      <div class="info-card">
        <i class="fas fa-phone-alt"></i>
        <h3>Call Us</h3>
        <p>+94 812 387 888</p>
      </div>
      <div class="info-card">
        <i class="fas fa-clock"></i>
        <h3>Hours</h3>
        <p>Mon-Fri: 11am - 8pm<br>Sat, Sun: 9am - 10pm</p>
      </div>
      <div class="info-card">
        <i class="fas fa-map-marker-alt"></i>
        <h3>Our Location</h3>
        <p>670/1/1A, Peradeniya Rd,<br>Kandy, Central 20000</p>
      </div>
    </div>
  </div>
</section>

<!-- Instagram Section -->
<section class="contact-instagram fadeIn">
  <h2>Follow Us On Instagram</h2>
  <div class="instagram-grid">
    <img src="${pageContext.request.contextPath}/external/instagram-post-1.jpg" alt="Instagram Post 1">
    <img src="${pageContext.request.contextPath}/external/instagram-post-2.jpg" alt="Instagram Post 2">
    <img src="${pageContext.request.contextPath}/external/instagram-post-3.jpg" alt="Instagram Post 3">
    <img src="${pageContext.request.contextPath}/external/instagram-post-4.jpg" alt="Instagram Post 4">
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

  // Form validation and toast notification
  document.getElementById('contactForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value;
    const toast = document.getElementById('toast');

    if (name && email && subject && message) {
      // Simulate form submission (replace with actual AJAX call)
      toast.textContent = "Message sent successfully!";
      toast.classList.add('show');
      this.reset();
    } else {
      toast.textContent = "Please fill out all fields.";
      toast.classList.add('show');
    }

    setTimeout(() => {
      toast.classList.remove('show');
    }, 2000);
  });

  // Pulse animation on info cards and submit button hover
  document.querySelectorAll('.info-card, .submit-btn').forEach(element => {
    element.addEventListener('mouseenter', function() {
      this.classList.add('pulse');
    });
    element.addEventListener('animationend', function() {
      this.classList.remove('pulse');
    });
  });
</script>
</body>
</html>