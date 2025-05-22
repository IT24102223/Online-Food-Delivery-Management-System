<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quick Bites - Admin User Management</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/external/letter-q.png">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-users.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
</head>
<body>

<div class="header-container">
    <div class="header-inner">
        <!-- Top Row: Logo and User Actions -->
        <div class="top-row">
            <!-- Logo -->
            <div class="logo-container">
                <img alt="japanesefood8198" src="${pageContext.request.contextPath}/external/japanesefood8198-lw1q.svg">
                <a>QuickBite</a>
            </div>
            <!-- User Actions -->
            <div class="login-signup">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <button class="logout-btn" onclick="window.location.href='${pageContext.request.contextPath}/user?action=logout'">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button class="login-btn" onclick="window.location.href='${pageContext.request.contextPath}/login.jsp'">
                            <i class="fas fa-sign-in-alt"></i> Login
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- Bottom Row: Navigation Links -->
        <div class="nav-container">
            <!-- Hamburger Menu Toggle (Visible on Mobile) -->
            <div class="hamburger" onclick="toggleMenu()">
                <div></div>
                <div></div>
                <div></div>
            </div>
            <!-- Navigation Menu -->
            <div class="nav-menu" id="nav-menu">
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Manage Users</a>
                <a href="${pageContext.request.contextPath}/admin/food-items" class="nav-link">Manage Food & Category</a>
                <a href="${pageContext.request.contextPath}/queue" class="nav-link">Manage Orders</a>
            </div>
        </div>
    </div>
</div>

<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>

<div class="container">
    <h1><i class="fas fa-users"></i> Manage Users</h1>
<%--    <div class="buttons" style="margin-bottom: 20px; text-align: right;">--%>
<%--        <button class="delete-btn" onclick="window.location.href='${pageContext.request.contextPath}/admin/food-items'">--%>
<%--            <i class="fas fa-utensils"></i> Manage Food Items--%>
<%--        </button>--%>
<%--    </div>--%>
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>
    <div class="card">
        <table class="user-table">
            <thead>
            <tr>
                <th><i class="fas fa-id-badge"></i> ID</th>
                <th><i class="fas fa-user"></i> Name</th>
                <th><i class="fas fa-envelope"></i> Email</th>
                <th><i class="fas fa-user-tag"></i> Role</th>
                <th><i class="fas fa-phone"></i> Phone</th>
                <th><i class="fas fa-map-marker-alt"></i> Address</th>
                <th><i class="fas fa-cog"></i> Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.userID}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.phoneNumber}</td>
                    <td>${user.address}</td>
                    <td>
                        <button class="delete-btn" onclick="showDeleteModal('${user.userID}', '${user.name}')">
                            <i class="fas fa-trash-alt"></i> Delete
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <h2><i class="fas fa-exclamation-triangle"></i> Confirm Deletion</h2>
        <p>Are you sure you want to delete user <span id="deleteUserName"></span>?</p>
        <input type="hidden" id="deleteUserId">
        <div class="modal-buttons">
            <button class="confirm-btn" onclick="confirmDelete()">Confirm</button>
            <button class="cancel-btn" onclick="closeDeleteModal()">Cancel</button>
        </div>
    </div>
</div>

<script>
    function showDeleteModal(userId, userName) {
        document.getElementById('deleteUserId').value = userId;
        document.getElementById('deleteUserName').innerText = userName;
        document.getElementById('deleteModal').style.display = 'flex';
    }

    function closeDeleteModal() {
        document.getElementById('deleteModal').style.display = 'none';
    }

    function confirmDelete() {
        const userId = document.getElementById('deleteUserId').value;
        const toast = document.getElementById('toast');

        fetch('${pageContext.request.contextPath}/admin/users?action=delete&id=' + userId, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    toast.textContent = 'User deleted successfully!';
                    toast.classList.add('show');
                    setTimeout(() => {
                        toast.classList.remove('show');
                        location.reload();
                    }, 2000);
                } else {
                    toast.textContent = 'Error: ' + data.error;
                    toast.classList.add('show');
                    setTimeout(() => {
                        toast.classList.remove('show');
                    }, 2000);
                }
                closeDeleteModal();
            })
            .catch(error => {
                toast.textContent = 'Failed to delete user: ' + error.message;
                toast.classList.add('show');
                setTimeout(() => {
                    toast.classList.remove('show');
                }, 2000);
                closeDeleteModal();
            });
    }

    window.onclick = function(event) {
        const modal = document.getElementById('deleteModal');
        if (event.target === modal) {
            closeDeleteModal();
        }
    };
</script>
</body>
</html>