<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Menu</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <h2>Menu</h2>
  <p>Select items to add to your cart.</p>
  <c:if test="${not empty foodItems}">
    <table class="table table-bordered">
      <thead>
      <tr>
        <th>Name</th>
        <th>Price</th>
        <th>Available</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="item" items="${foodItems}">
        <tr>
          <td>${item.name}</td>
          <td>${item.price}</td>
          <td>${item.available ? 'Yes' : 'No'}</td>
          <td>
            <c:if test="${item.available}">
              <form action="${pageContext.request.contextPath}/order/add-to-cart" method="post">
                <input type="hidden" name="itemId" value="${item.itemId}">
                <input type="number" name="quantity" value="1" min="1" class="form-control d-inline" style="width: 80px;">
                <button type="submit" class="btn btn-sm btn-success">Add to Cart</button>
              </form>
            </c:if>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>
  <c:if test="${empty foodItems}">
    <p>No items available in the menu.</p>
  </c:if>
  <a href="${pageContext.request.contextPath}/order/create" class="btn btn-primary">Proceed to Checkout</a>
  <a href="${pageContext.request.contextPath}/order/list" class="btn btn-secondary">Back to Orders</a>
</div>
</body>
</html>