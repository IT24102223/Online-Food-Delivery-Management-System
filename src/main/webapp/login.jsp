<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login QuickBite</title>
  <link rel="icon" type="image/x-icon" href="external/letter-q.png">
  <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One&display=swap">
  <!-- Font Awesome for Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<%@ include file="header.jsp" %>
<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>
<div class="container">
  <div class="main-content">
    <div class="product-card">
      <h2><i class="fas fa-sign-in-alt"></i> Login</h2>
      <c:if test="${not empty error}">
        <p class="price">${error}</p>
      </c:if>
      <form id="loginForm" action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="action" value="login">
        <div class="input-group">
          <label for="email"><i class="fas fa-envelope"></i> Email</label>
          <input type="email" id="email" name="email" placeholder="Enter Email" required>
        </div>
        <div class="input-group">
          <label for="password"><i class="fas fa-lock"></i> Password</label>
          <input type="password" id="password" name="password" placeholder="Enter Password" required>
        </div>
        <div class="buttons">
          <button type="submit" class="order-now">Login <i class="fas fa-sign-in-alt"></i></button>
        </div>
        <p class="rating"><a href="${pageContext.request.contextPath}/forgot-password.jsp">Forgot Password?</a></p>
      </form>
    </div>
  </div>
</div>
<%@ include file="footer.jsp" %>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const toast = document.getElementById('toast');

    if (!loginForm || !toast) {
      console.error('Required elements not found: loginForm or toast');
      return;
    }

    loginForm.addEventListener('submit', function(event) {
      event.preventDefault();
      const button = this.querySelector('.order-now');
      if (!button) {
        console.error('Submit button not found');
        return;
      }

      button.innerHTML = `<div class="spinner"></div> Logging in...`;
      button.disabled = true;

      const formData = new FormData(loginForm);
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
                    toast.textContent = "Logged in successfully!";
                    toast.classList.add('show');
                    setTimeout(() => {
                      toast.classList.remove('show');
                      window.location.href = data.redirectUrl;
                    }, 2000);
                  } else {
                    toast.textContent = data.error || "Login failed!";
                    toast.classList.add('show');
                    setTimeout(() => toast.classList.remove('show'), 2000);
                  }
                })
                .catch(error => {
                  console.error('Error during login:', error);
                  toast.textContent = "Error during login: " + error.message;
                  toast.classList.add('show');
                  setTimeout(() => toast.classList.remove('show'), 2000);
                })
                .finally(() => {
                  button.innerHTML = `Login <i class="fas fa-sign-in-alt"></i>`;
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