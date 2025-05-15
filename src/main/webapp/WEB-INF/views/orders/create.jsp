<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Order</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 20px;
        }
        .container {
            display: flex;
            justify-content: space-between;
            max-width: 900px;
            margin: 0 auto;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .payment-method, .order-summary {
            width: 48%;
        }
        .payment-method h2, .order-summary h2 {
            font-size: 18px;
            color: #333;
            margin-bottom: 20px;
        }
        .card-preview {
            background-color: #1e88e5;
            color: white;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 20px;
            position: relative;
        }
        .card-preview .card-number {
            font-size: 18px;
            letter-spacing: 2px;
        }
        .card-preview .card-holder {
            font-size: 14px;
            margin-top: 10px;
        }
        .card-preview .edit-icon {
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            font-size: 14px;
            color: #666;
            margin-bottom: 5px;
        }
        .form-group select, .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        .card-details {
            display: flex;
            justify-content: space-between;
        }
        .card-details .form-group {
            width: 48%;
        }
        .card-logos {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        .card-logos img {
            height: 30px;
        }
        .table {
            width: 100%;
            font-size: 14px;
            margin-bottom: 20px;
        }
        .table th, .table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .table th {
            font-weight: bold;
            color: #333;
        }
        .summary-row {
            display: flex;
            justify-content: space-between;
            font-size: 14px;
            margin-bottom: 10px;
        }
        .summary-row.total {
            font-weight: bold;
            font-size: 16px;
        }
        .order-btn {
            background-color: #ffca28;
            color: #333;
            padding: 15px;
            border: none;
            border-radius: 5px;
            width: 100%;
            font-size: 16px;
            cursor: pointer;
        }
        .back-btn {
            background-color: #f5f5f5;
            color: #333;
            padding: 15px;
            border: none;
            border-radius: 5px;
            width: 100%;
            font-size: 16px;
            text-align: center;
            text-decoration: none;
            display: block;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="payment-method">
        <h2>Payment method</h2>
        <div class="card-preview">
            <div class="card-number">1234 5678 9876 5432</div>
            <div class="card-holder">AL HOLDER</div>
            <div class="edit-icon">+</div>
        </div>
        <div class="form-group">
            <label>Use saved card:</label>
            <select>
                <option>Mastercard</option>
                <option>Visa</option>
            </select>
        </div>
        <div class="form-group">
            <label>Name on card:</label>
            <input type="text" value="Esther Howard" readonly>
        </div>
        <div class="form-group">
            <label>Card number:</label>
            <input type="text" value="123-456-789" readonly>
        </div>
        <div class="card-details">
            <div class="form-group">
                <label>Expiry date:</label>
                <input type="text" placeholder="MM / YY">
            </div>
            <div class="form-group">
                <label>CCV:</label>
                <input type="text" placeholder="***">
            </div>
        </div>
        <div class="card-logos">
            <img src="https://upload.wikimedia.org/wikipedia/commons/0/04/Visa.svg" alt="Visa">
            <img src="https://upload.wikimedia.org/wikipedia/commons/4/41/Mastercard_Logo.svg" alt="Mastercard">
            <img src="https://upload.wikimedia.org/wikipedia/commons/8/89/American_Express_logo_%282018%29.svg" alt="American Express">
        </div>
    </div>
    <div class="order-summary">
        <h2>Order summary</h2>
        <form action="${pageContext.request.contextPath}/order/create" method="post">
            <c:if test="${not empty sessionScope.user.cart.items}">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Item Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Subtotal</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="cartItem" items="${sessionScope.user.cart.items}">
                        <tr>
                            <td>${cartItem.foodItem.name}</td>
                            <td>${cartItem.quantity}</td>
                            <td>${cartItem.foodItem.price}</td>
                            <td>${cartItem.quantity * cartItem.foodItem.price}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="summary-row total">
                    <span>Total</span>
                    <span>${sessionScope.user.cart.total}</span>
                </div>
            </c:if>
            <c:if test="${empty sessionScope.user.cart.items}">
                <p class="summary-row">Your cart is empty. Please add items to your cart before placing an order.</p>
            </c:if>
            <button type="submit" class="order-btn" <c:if test="${empty sessionScope.user.cart.items}">disabled</c:if>>Confirm Order</button>
            <a href="${pageContext.request.contextPath}/order/list" class="back-btn">Back to Orders</a>
        </form>
    </div>
</div>
</body>
</html>