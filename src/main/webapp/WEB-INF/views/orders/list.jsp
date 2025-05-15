<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
        }
        .sidebar {
            width: 250px;
            height: 100vh;
            background-color: #fff;
            padding: 20px;
            position: fixed;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        }
        .sidebar a {
            display: block;
            padding: 10px;
            color: #333;
            text-decoration: none;
            font-size: 14px;
        }
        .sidebar a.active {
            background-color: #ff6200;
            color: #fff;
            border-radius: 5px;
        }
        .sidebar a:hover {
            background-color: #f5f5f5;
            border-radius: 5px;
        }
        .content {
            margin-left: 270px;
            padding: 20px;
        }
        .table-container {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .table th {
            background-color: #f8f9fa;
            font-size: 14px;
        }
        .table td {
            font-size: 14px;
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
        .btn-primary {
            background-color: #ffca28;
            border-color: #ffca28;
            color: #333;
        }
        .btn-primary:hover {
            background-color: #ffb300;
            border-color: #ffb300;
        }
        .btn-info {
            font-size: 12px;
            padding: 5px 10px;
        }
        .no-orders {
            font-size: 14px;
            color: #666;
        }
    </style>
</head>
<body>
<div class="sidebar">
    <a href="#" class="active">Order History</a>
    <a href="#">Dashboard</a>
    <a href="#">Track Order</a>
    <a href="#">Shopping Cart</a>
    <a href="#">Wishlist</a>
    <a href="#">Compare</a>
    <a href="#">Cards & Address</a>
    <a href="#">Browsing History</a>
    <a href="#">Settings</a>
    <a href="#">Logout</a>
</div>
<div class="content">
    <div class="table-container">
        <h2>Your Orders</h2>
        <a href="${pageContext.request.contextPath}/order/create" class="btn btn-primary mb-3">Place New Order</a>
        <c:if test="${not empty orders}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Order Date</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>${order.total}</td>
                        <td><span class="status-${fn:toLowerCase(order.status)}"><c:out value="${order.status}" default="Unknown"/></span></td>
                        <td>${order.orderDate}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/order/details?id=${order.orderId}" class="btn btn-sm btn-info">View</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty orders}">
            <p class="no-orders">You have no orders yet.</p>
        </c:if>
    </div>
</div>
</body>
</html>