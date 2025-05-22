<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.util.List, model.Order, service.OrderService, jakarta.servlet.http.HttpSession, model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders QuickBite</title>
    <link rel="icon" type="image/x-icon" href="external/letter-q.png">
    <!-- Google Fonts (Poppins, DM Sans, Patua One) -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/myorders.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>

<div class="container">
    <div class="main-content fadeIn">
        <div class="header">
            <h2>My Orders</h2>
        </div>
        <div class="order-grid fadeIn">
            <c:if test="${empty orders}">
                <p>No orders found.</p>
            </c:if>
            <c:forEach var="order" items="${orders}">
                <div class="order-card">
                    <h3>Order #${order.orderId}</h3>
                    <p><i class="fas fa-calendar"></i> Date: ${order.orderDate}</p>
                    <p><i class="fas fa-money-bill-wave"></i> Total: ${String.format("%.2f", order.totalAmount)} LKR</p>
                    <p><i class="fas fa-info-circle"></i> Status: ${order.status}</p>
                    <div class="items-list">
                        <c:forEach var="item" items="${order.items}">
                            <p>${item.name} x 1 - ${String.format("%.2f", item.price)} LKR</p>
                        </c:forEach>
                    </div>
                    <c:if test="${order.status == 'Pending'}">
                        <button class="cancel-order" data-order-id="${order.orderId}">Cancel Order <i class="fas fa-times"></i></button>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

<script>
    // AJAX for Cancel Order
    document.querySelectorAll('.cancel-order').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const orderId = this.getAttribute('data-order-id');
            const toast = document.getElementById('toast');

            fetch('${pageContext.request.contextPath}/order', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=cancel&orderId=' + encodeURIComponent(orderId)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(data => {
                    toast.textContent = "Order cancelled successfully!";
                    toast.classList.add('show');
                    // Reload immediately after showing toast
                    setTimeout(() => {
                        toast.classList.remove('show');
                        location.reload();
                    }, 2000); // 2-second delay to show toast
                })
                .catch(error => {
                    toast.textContent = "Error cancelling order!";
                    toast.classList.add('show');
                    setTimeout(() => {
                        toast.classList.remove('show');
                    }, 2000);
                });

            // Add pulse and bounce animations
            this.classList.remove('pulse');
            this.classList.add('pulse');
            const icon = this.querySelector('i');
            if (icon) {
                icon.classList.remove('bounce');
                void icon.offsetWidth;
                icon.classList.add('bounce');
            }
        });
    });

    // Show toast only for initial page load errors
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const toast = document.getElementById('toast');
        if (urlParams.get('error')) {
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