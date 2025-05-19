<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        h2 {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
        }
        .order-details p {
            font-size: 14px;
            color: #666;
            margin: 5px 0;
        }
        .order-details p strong {
            color: #333;
        }
        .table {
            font-size: 14px;
            margin-bottom: 20px;
        }
        .table th, .table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .status-pending {
            color: #ffc107;
        }
        .status-processing {
            color: #007bff;
        }
        .status-delivered {
            color: #28a745;
        }
        .status-cancelled {
            color: #dc3545;
        }
        .btn-back {
            background-color: #f5f5f5;
            color: #333;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
        }
        .btn-action {
            font-size: 12px;
            padding: 5px 10px;
            margin-right: 5px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Order Details</h2>
    <div class="order-details">
        <p><strong>Order ID:</strong> #${order.orderId}</p>
        <p><strong>Customer:</strong> ${order.customer.username}</p>
        <p><strong>Total:</strong> $${String.format("%.2f", order.total)}</p>
        <p><strong>Status:</strong> <span id="orderStatus" class="status-${fn:toLowerCase(order.status)}">${order.status}</span></p>
        <p><strong>Order Date:</strong> ${order.orderDate}</p>
        <p><strong>Delivery Date:</strong> ${order.deliveryDate != null ? order.deliveryDate : 'N/A'}</p>
        <h4>Order Items</h4>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>Item Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Subtotal</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${order.items}">
                <tr>
                    <td>${item.foodItem.name}</td>
                    <td>${item.quantity}</td>
                    <td>$${String.format("%.2f", item.foodItem.price)}</td>
                    <td>$${String.format("%.2f", item.quantity * item.foodItem.price)}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${order.status != 'DELIVERED' && order.status != 'CANCELLED'}">
            <form action="${pageContext.request.contextPath}/order/cancel" method="post" style="display: inline;">
                <input type="hidden" name="id" value="${order.orderId}">
                <button type="submit" class="btn btn-danger btn-action">Cancel Order</button>
            </form>
            <form action="${pageContext.request.contextPath}/order/update" method="post" style="display: inline;">
                <input type="hidden" name="id" value="${order.orderId}">
                <select name="status">
                    <option value="PROCESSING" <c:if test="${order.status == 'PROCESSING'}">selected</c:if>>Processing</option>
                    <option value="DELIVERED" <c:if test="${order.status == 'DELIVERED'}">selected</c:if>>Delivered</option>
                </select>
                <button type="submit" class="btn btn-primary btn-action">Update Status</button>
            </form>
        </c:if>
    </div>
    <a href="${pageContext.request.contextPath}/order/list" class="btn-back">Back to Orders</a>
</div>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const orderId = '${order.orderId}';
        const statusSpan = document.getElementById('orderStatus');
        const pollStatus = () => {
            fetch('${pageContext.request.contextPath}/order/status?id=' + encodeURIComponent(orderId))
                .then(response => response.json())
                .then(data => {
                    if (data.status) {
                        statusSpan.textContent = data.status;
                        statusSpan.className = 'status-' + data.status.toLowerCase();
                        if (data.status === 'DELIVERED' || data.status === 'CANCELLED') {
                            document.querySelectorAll('form').forEach(form => form.remove());
                        }
                    }
                })
                .catch(error => console.error('Error polling status:', error));
        };
        setInterval(pollStatus, 10000);
        pollStatus();
    });
</script>
</body>
</html>