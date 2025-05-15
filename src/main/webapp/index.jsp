<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Customer" %>
<!DOCTYPE html>
<html>
<head>
    <title>Food Delivery System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<%
    // Check if a user is logged in
    Customer customer = (Customer) session.getAttribute("user");
    if (customer != null) {
        // Redirect to order list if logged in
        response.sendRedirect(request.getContextPath() + "/order/list");
    } else {
        // Redirect to login page if not logged in
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>
</body>
</html>