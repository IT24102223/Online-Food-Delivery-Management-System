<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sign Up QuickBite</title>
  <link rel="icon" type="image/x-icon" href="external/letter-q.png">
  <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One&display=swap">
  <!-- Font Awesome for Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
</head>
<body>
<%@ include file="header.jsp" %>
<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>
<div class="container">
  <div class="main-content">
    <div class="product-card">
      <h2><i class="fas fa-user-plus"></i> Sign Up</h2>
      <c:if test="${not empty error}">
        <p class="price">${error}</p>
      </c:if>
      <form id="signupForm" action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="register">
        <div class="input-group">
          <label for="name"><i class="fas fa-user"></i> Name</label>
          <input type="text" id="name" name="name" placeholder="Enter Name" required>
        </div>
        <div class="input-group">
          <label for="email"><i class="fas fa-envelope"></i> Email</label>
          <input type="email" id="email" name="email" placeholder="Enter Email" required>
        </div>
        <div class="input-group">
          <label for="password"><i class="fas fa-lock"></i> Password</label>
          <input type="password" id="password" name="password" placeholder="Enter Password" required>
        </div>
        <div class="input-group">
          <label for="phone"><i class="fas fa-phone"></i> Phone</label>
          <input type="tel" id="phone" name="phoneNumber" placeholder="Enter Phone" pattern="[0-9]{10}" required>
        </div>
        <div class="input-group">
          <label for="address"><i class="fas fa-map-marker-alt"></i> Address</label>
          <textarea id="address" name="address" placeholder="Enter Address" required></textarea>
        </div>
        <div class="buttons">
          <button type="submit" class="order-now">Sign Up <i class="fas fa-user-plus"></i></button>
        </div>
      </form>
    </div>
  </div>
</div>
<%@ include file="footer.jsp" %>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const signupForm = document.getElementById('signupForm');
    const toast = document.getElementById('toast');

    if (!signupForm || !toast) {
      console.error('Required elements not found: signupForm or toast');
      return;
    }

    signupForm.addEventListener('submit', function(event) {
      event.preventDefault();
      const button = this.querySelector('.order-now');
      if (!button) {
        console.error('Submit button not found');
        return;
      }

      button.innerHTML = `<div class="spinner"></div> Signing up...`;
      button.disabled = true;

      const formData = new FormData(signupForm);
      const params = new URLSearchParams(formData);

      setTimeout(() => {
        fetch('${pageContext.request.contextPath}/user', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: params.toString()
        })
                .then(response => {
                  console.log('Response Status:', response.status);
                  if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                  }
                  return response.json();
                })
                .then(data => {
                  console.log('Response Data:', data);
                  if (data.success) {
                    toast.textContent = "Signed up successfully!";
                    toast.classList.add('show');
                    setTimeout(() => {
                      toast.classList.remove('show');
                      window.location.href = data.redirectUrl;
                    }, 2000);
                  } else {
                    toast.textContent = data.error || "Sign up failed!";
                    toast.classList.add('show');
                    setTimeout(() => toast.classList.remove('show'), 2000);
                  }
                })
                .catch(error => {
                  console.error('Error during signup:', error);
                  toast.textContent = "Error during signup: " + error.message;
                  toast.classList.add('show');
                  setTimeout(() => toast.classList.remove('show'), 2000);
                })
                .finally(() => {
                  button.innerHTML = `Sign Up <i class="fas fa-user-plus"></i>`;
                  button.disabled = false;
                });
      }, 100);
    });

    function showToast(message) {
      if (!toast) return;
      toast.textContent = message;
      toast.classList.add('show');
      setTimeout(() => toast.classList.remove('show'), 2000);
    }
  });
</script>
</body>
</html>