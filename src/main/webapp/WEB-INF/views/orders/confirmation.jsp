<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Order Confirmed</h2>
    <div class="alert alert-success">
        Your food is on its way! Thank you for your order.
    </div>
    <a href="${pageContext.request.contextPath}/order/create" class="btn btn-primary">Add Another Order</a>
    <a href="${pageContext.request.contextPath}/order/list" class="btn btn-secondary">View Orders</a>
</div>
</body>
</html>