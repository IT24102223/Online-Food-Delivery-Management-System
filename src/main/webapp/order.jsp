<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Order Confirmation</title>
  <link rel="icon" type="image/x-icon" href="external/letter-q.png">
  <!-- Google Fonts (Poppins, DM Sans, Patua One) -->
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
  <!-- Font Awesome for Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <!-- Custom CSS -->
  <link rel="stylesheet" href="css/order.css">
  <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>

<div class="container">
  <div class="main-content fadeIn">
    <div class="header">
      <h2>Order Confirmation</h2>
    </div>
    <div class="order-details">
      <c:if test="${not empty cartItems}">
        <c:forEach var="cartItem" items="${cartItems}">
          <div class="order-item">
            <span>${cartItem.foodItem.name} x ${cartItem.quantity}</span>
            <span>${String.format("%.2f", cartItem.foodItem.price * cartItem.quantity)} LKR</span>
          </div>
        </c:forEach>
        <div class="order-total">
          <span>Subtotal:</span>
          <span>${subtotal} LKR</span>
        </div>
        <div class="order-total">
          <span>Discount (20%):</span>
          <span>${discount} LKR</span>
        </div>
        <div class="order-total">
          <span>Delivery Fee:</span>
          <span>${deliveryFee}</span>
        </div>
        <div class="order-total">
          <span>Total:</span>
          <span>${total} LKR</span>
        </div>
      </c:if>
      <c:if test="${empty cartItems}">
        <p>No items in cart to order.</p>
      </c:if>
      <form action="${pageContext.request.contextPath}/order" method="post">
        <input type="hidden" name="action" value="place">
        <div class="delivery-address">
          <label for="deliveryAddress">Delivery Address:</label>
          <input type="text" id="deliveryAddress" name="deliveryAddress" value="${user.address}" required>
        </div>
        <div class="payment-section">
          <h4>Payment Method</h4>
          <div class="payment-options">
            <label class="custom-radio">
              <input type="radio" name="payment" value="cod" checked>
              <span>Cash on Delivery</span>
            </label>
            <label class="custom-radio">
              <input type="radio" name="payment" value="card">
              <span>Credit/Debit Card</span>
            </label>
          </div>
          <div class="payment-info">
            <p>Pay with Card (Visa or MasterCard)</p>
            <div class="card-logos">
              <img src="images/visa_logo.png" alt="Visa" class="card-logo">
              <img src="images/mastercard_logo.png" alt="MasterCard" class="card-logo">
            </div>
          </div>
        </div>
        <c:if test="${not empty cartItems}">
          <button type="submit" class="place-order-btn">Place Order <i class="fas fa-check"></i></button>
        </c:if>
      </form>
      <c:if test="${not empty param.error}">
        <p class="error-message">${param.error}</p>
      </c:if>
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>

<script>
  // Show toast notification for order success
  window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const toast = document.getElementById('toast');
    if (urlParams.get('orderPlaced') === 'true') {
      toast.textContent = "Order placed successfully!";
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 2000);
    } else if (urlParams.get('error')) {
      toast.textContent = urlParams.get('error');
      toast.classList.add('show');
      setTimeout(() => {
        toast.classList.remove('show');
      }, 2000);
    }
  };
</script>
</body>
</html>