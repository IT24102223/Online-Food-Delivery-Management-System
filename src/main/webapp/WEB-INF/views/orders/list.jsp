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
    <a href="${pageContext.request.contextPath}/order/list" class="active">Order History</a>
    <a href="${pageContext.request.contextPath}/order/menu">Menu</a>
    <a href="${pageContext.request.contextPath}/order/create">Cart</a>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</div>
<div class="content">
    <div class="table-container">
        <h2>Your Orders</h2>
        <div class="mb-3 d-flex justify-content-between">
            <form action="${pageContext.request.contextPath}/order/list" method="get" class="d-flex align-items-center">
                <input type="text" name="search" class="form-control me-2" placeholder="Search by Order ID or Status" value="${param.search}">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
            <form action="${pageContext.request.contextPath}/order/list" method="get" class="d-flex align-items-center">
                <select name="sort" class="form-select me-2" style="width: 200px;">
                    <option value="date_desc" ${param.sort == 'date_desc' ? 'selected' : ''}>Newest First</option>
                    <option value="date_asc" ${param.sort == 'date_asc' ? 'selected' : ''}>Oldest First</option>
                    <option value="status" ${param.sort == 'status' ? 'selected' : ''}>Status</option>
                </select>
                <button type="submit" class="btn btn-primary">Sort</button>
            </form>
        </div>
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
                <c:forEach var="order" items="${orders}" begin="${param.page != null ? (param.page - 1) * 10 : 0}" end="${param.page != null ? param.page * 10 - 1 : 9}">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>$${String.format("%.2f", order.total)}</td>
                        <td><span class="status-${fn:toLowerCase(order.status)}"><c:out value="${order.status}" default="Unknown"/></span></td>
                        <td>${order.orderDate}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/order/details?id=${order.orderId}" class="btn btn-sm btn-info">View</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="d-flex justify-content-between mt-3">
                <c:if test="${param.page != null && param.page > 1}">
                    <a href="${pageContext.request.contextPath}/order/list?page=${param.page - 1}&sort=${param.sort}&search=${param.search}" class="btn btn-secondary">Previous</a>
                </c:if>
                <c:if test="${orders.size() > (param.page != null ? param.page * 10 : 10)}">
                    <a href="${pageContext.request.contextPath}/order/list?page=${param.page != null ? param.page + 1 : 2}&sort=${param.sort}&search=${param.search}" class="btn btn-secondary">Next</a>
                </c:if>
            </div>
        </c:if>
        <c:if test="${empty orders}">
            <p class="no-orders">You have no orders yet.</p>
        </c:if>
    </div>
</div>
</body>
</html>