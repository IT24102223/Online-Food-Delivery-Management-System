<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cart QuickBite</title>
    <link rel="icon" type="image/x-icon" href="external/letter-q.png">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/cart.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<div class="container">
    <h1>Your Cart</h1>
    <div class="cart-content fadeIn">
        <!-- Cart Items -->
        <div class="cart-items">
            <c:choose>
                <c:when test="${empty cartItems}">
                    <div class="empty-cart"><p>Your cart is empty.</p></div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="cartItem" items="${cartItems}" varStatus="loop">
                        <div class="cart-item">
                            <img src="images/${cartItem.foodItem.foodId}.avif" alt="${cartItem.foodItem.name}">
                            <div class="item-details">
                                <h3>${cartItem.foodItem.name}</h3>
                                <p>${cartItem.foodItem.description}</p>
                            </div>
                            <span class="item-price">${cartItem.foodItem.price} LKR</span>
                            <div class="quantity-controls">
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="index" value="${loop.index}">
                                    <input type="hidden" name="change" value="-1">
                                    <button type="submit" class="quantity-btn">-</button>
                                </form>
                                <span class="quantity">${cartItem.quantity}</span>
                                <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="index" value="${loop.index}">
                                    <input type="hidden" name="change" value="1">
                                    <button type="submit" class="quantity-btn">+</button>
                                </form>
                            </div>
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="index" value="${loop.index}">
                                <button type="submit" class="delete-btn"><i class="fas fa-trash"></i></button>
                            </form>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Order Summary -->
        <div class="order-summary">
            <h2>Order Summary</h2>
            <div class="summary-row">
                <span>Subtotal</span>
                <span>${subtotal} LKR</span>
            </div>
            <div class="summary-row discount">
                <span>Discount (20%)</span>
                <span>${discount} LKR</span>
            </div>
            <div class="summary-row">
                <span>Delivery Fee</span>
                <span>${deliveryFee}</span>
            </div>
            <div class="summary-row total">
                <span>Total</span>
                <span>${total} LKR</span>
            </div>
            <form action="${pageContext.request.contextPath}/order" method="get">
                <button type="submit" class="checkout-btn">
                    <span>Go to Checkout</span>
                    <i class="fas fa-arrow-right"></i>
                </button>
            </form>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

<script>
    // Toast notification for cart actions
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const toast = document.getElementById('toast');
        if (urlParams.get('updated') === 'true') {
            toast.textContent = "Cart updated successfully!";
            toast.classList.add('show');
            setTimeout(() => {
                toast.classList.remove('show');
            }, 2000);
        } else if (urlParams.get('deleted') === 'true') {
            toast.textContent = "Item removed from cart!";
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