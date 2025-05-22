<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quick Bites - Admin Food & Category Management</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/external/letter-q.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Google Fonts (matching foods.jsp) -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;700&display=swap">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
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
                        <%--                        <!-- User is logged in -->--%>
                        <%--                        <button class="profile-btn" onclick="window.location.href='${pageContext.request.contextPath}/user?action=profile'">--%>
                        <%--                            <i class="fas fa-user"></i> Profile--%>
                        <%--                        </button>--%>
                        <button class="logout-btn" onclick="window.location.href='${pageContext.request.contextPath}/user?action=logout'">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </button>
                    </c:when>
                    <c:otherwise>
                        <!-- User is not logged in -->
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

<div class="dashboard-container">
    <h1>Manage Food & Category</h1>

    <!-- Error/Success Messages -->
    <c:if test="${not empty error}">
        <p class="error"><i class="fas fa-exclamation-circle"></i> ${error}</p>
    </c:if>
    <c:if test="${not empty success}">
        <p class="success"><i class="fas fa-check-circle"></i> ${success}</p>
    </c:if>

    <!-- Add Category Section -->
    <div class="section">
        <h2><i class="fas fa-plus-circle"></i> Add New Category</h2>
        <form action="${pageContext.request.contextPath}/admin/food-items" method="post">
            <input type="hidden" name="action" value="addCategory">
            <div class="form-group">
                <label for="categoryId"><i class="fas fa-id-badge"></i> Category ID:</label>
                <input type="text" id="categoryId" name="categoryId" value="${param.categoryId}" required>
            </div>
            <div class="form-group">
                <label for="categoryName"><i class="fas fa-tag"></i> Name:</label>
                <input type="text" id="categoryName" name="categoryName" value="${param.categoryName}" required>
            </div>
            <div class="form-group">
                <label for="categoryDescription"><i class="fas fa-info-circle"></i> Description:</label>
                <textarea id="categoryDescription" name="categoryDescription" rows="3">${param.categoryDescription}</textarea>
            </div>
            <div class="form-buttons">
                <input type="submit" value="Add Category" class="apply-filter">
                <a href="${pageContext.request.contextPath}/admin/food-items" class="reset-filter">Clear <i class="fas fa-undo" style="margin-left: 5px;"></i></a>
            </div>
        </form>
    </div>

    <!-- Categories List Section -->
    <div class="section">
        <h2><i class="fas fa-list"></i> Categories List</h2>
        <table>
            <tr>
                <th><i class="fas fa-id-badge"></i> ID</th>
                <th><i class="fas fa-tag"></i> Name</th>
                <th><i class="fas fa-info-circle"></i> Description</th>
                <th><i class="fas fa-cog"></i> Actions</th>
            </tr>
            <c:choose>
                <c:when test="${not empty categories}">
                    <c:forEach items="${categories}" var="category">
                        <tr>
                            <td>${category.categoryId}</td>
                            <td>${category.name}</td>
                            <td>${category.description}</td>
                            <td class="actions">
                                <a href="#" class="edit-category-btn" data-category-id="${category.categoryId}" data-name="${category.name}" data-description="${category.description}"><i class="fas fa-edit"></i> Edit</a>
                                <a href="#" class="delete-btn" data-delete-url="${pageContext.request.contextPath}/admin/food-items?action=deleteCategory&id=${category.categoryId}" data-item-name="${category.name}"><i class="fas fa-trash"></i> Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">No categories available.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
    </div>

    <!-- Add Food Item Section -->
    <div class="section">
        <h2><i class="fas fa-plus-circle"></i> Add New Food Item</h2>
        <form action="${pageContext.request.contextPath}/admin/food-items" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label for="foodId"><i class="fas fa-id-badge"></i> Food ID:</label>
                <input type="text" id="foodId" name="foodId" value="${param.foodId}" required>
            </div>
            <div class="form-group">
                <label for="name"><i class="fas fa-tag"></i> Name:</label>
                <input type="text" id="name" name="name" value="${param.name}" required>
            </div>
            <div class="form-group">
                <label for="description"><i class="fas fa-info-circle"></i> Description:</label>
                <textarea id="description" name="description" rows="3">${param.description}</textarea>
            </div>
            <div class="form-group">
                <label for="price"><i class="fas fa-dollar-sign"></i> Price:</label>
                <input type="number" id="price" name="price" step="0.01" min="0" value="${param.price}" required>
            </div>
            <div class="form-group">
                <label for="foodCategoryId"><i class="fas fa-list"></i> Category:</label>
                <select id="foodCategoryId" name="categoryId" required>
                    <option value="">Select a category</option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.categoryId}" ${param.categoryId eq category.categoryId ? 'selected' : ''}>
                                ${category.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="availability"><i class="fas fa-check-circle"></i> Available:</label>
                <input type="checkbox" id="availability" name="availability" value="true" ${param.availability eq 'true' ? 'checked' : ''}>
            </div>
            <div class="form-buttons">
                <input type="submit" value="Add Item" class="apply-filter">
                <a href="${pageContext.request.contextPath}/admin/food-items" class="reset-filter">Clear <i class="fas fa-undo" style="margin-left: 5px;"></i></a>
            </div>
        </form>
    </div>

    <!-- Sort Section -->
    <div class="sort">
        <span><i class="fas fa-sort"></i> Sort by:</span>
        <form id="sortForm" action="${pageContext.request.contextPath}/admin/food-items" method="get">
            <select name="sortBy" onchange="document.getElementById('sortForm').submit()">
                <option value="foodId" ${sortBy == 'foodId' ? 'selected' : ''}>Food ID</option>
                <option value="highToLow" ${sortBy == 'highToLow' ? 'selected' : ''}>Price High to Low</option>
                <option value="lowToHigh" ${sortBy == 'lowToHigh' ? 'selected' : ''}>Price Low to High</option>
            </select>
            <input type="hidden" name="page" value="${currentPage != null ? currentPage : '1'}">
        </form>
    </div>

    <!-- Food Items List Section -->
    <div class="section">
        <h2><i class="fas fa-list"></i> Food Items List</h2>
        <table>
            <tr>
                <th><i class="fas fa-id-badge"></i> ID</th>
                <th><i class="fas fa-tag"></i> Name</th>
                <th><i class="fas fa-info-circle"></i> Description</th>
                <th><i class="fas fa-dollar-sign"></i> Price</th>
                <th><i class="fas fa-list"></i> Category</th>
                <th><i class="fas fa-check-circle"></i> Available</th>
                <th><i class="fas fa-cog"></i> Actions</th>
            </tr>
            <c:choose>
                <c:when test="${not empty foodItems}">
                    <c:forEach items="${foodItems}" var="item">
                        <tr>
                            <td>${item.foodId}</td>
                            <td>${item.name}</td>
                            <td>${item.description}</td>
                            <td>${String.format("%.2f", item.price)}</td>
                            <td>
                                <c:forEach items="${categories}" var="category">
                                    <c:if test="${category.categoryId eq item.categoryId}">
                                        ${category.name}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>${item.available ? 'Yes' : 'No'}</td>
                            <td class="actions">
                                <a href="#" class="edit-btn" data-food-id="${item.foodId}" data-name="${item.name}" data-description="${item.description}" data-price="${item.price}" data-category-id="${item.categoryId}" data-available="${item.available}"><i class="fas fa-edit"></i> Edit</a>
                                <a href="#" class="delete-btn" data-delete-url="${pageContext.request.contextPath}/admin/food-items?action=delete&id=${item.foodId}" data-item-name="${item.name}"><i class="fas fa-trash"></i> Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7">No food items available.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>

        <!-- Pagination -->
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/admin/food-items?page=${currentPage - 1}&sortBy=${sortBy}" class="pagination-link">
                    <button class="pagination-btn"><i class="fas fa-chevron-left"></i> Previous</button>
                </a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="${pageContext.request.contextPath}/admin/food-items?page=${i}&sortBy=${sortBy}" class="pagination-link">
                    <span class="pagination-number ${currentPage == i ? 'active' : ''}">${i}</span>
                </a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/admin/food-items?page=${currentPage + 1}&sortBy=${sortBy}" class="pagination-link">
                    <button class="pagination-btn">Next <i class="fas fa-chevron-right"></i></button>
                </a>
            </c:if>
        </div>
    </div>
</div>

<!-- Edit Food Item Modal -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <span class="close-btn">×</span>
        <h2><i class="fas fa-edit"></i> Edit Food Item</h2>
        <form id="editForm" action="${pageContext.request.contextPath}/admin/food-items" method="post">
            <input type="hidden" name="action" value="update">
            <div class="form-group">
                <label for="editFoodId"><i class="fas fa-id-badge"></i> Food ID:</label>
                <input type="text" id="editFoodId" name="foodId" readonly>
            </div>
            <div class="form-group">
                <label for="editName"><i class="fas fa-tag"></i> Name:</label>
                <input type="text" id="editName" name="name" required>
            </div>
            <div class="form-group">
                <label for="editDescription"><i class="fas fa-info-circle"></i> Description:</label>
                <textarea id="editDescription" name="description" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label for="editPrice"><i class="fas fa-dollar-sign"></i> Price:</label>
                <input type="number" id="editPrice" name="price" step="0.01" min="0" required>
            </div>
            <div class="form-group">
                <label for="editFoodCategoryId"><i class="fas fa-list"></i> Category:</label>
                <select id="editFoodCategoryId" name="categoryId" required>
                    <option value="">Select a category</option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.categoryId}">${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="editAvailability"><i class="fas fa-check-circle"></i> Available:</label>
                <input type="checkbox" id="editAvailability" name="availability" value="true">
            </div>
            <div class="form-buttons">
                <input type="submit" value="Update" class="apply-filter"><i class="fas fa-save"></i>
                <button type="button" class="reset-filter close-btn"><i class="fas fa-times"></i> Cancel</button>
            </div>
        </form>
    </div>
</div>

<!-- Edit Category Modal -->
<div id="editCategoryModal" class="modal">
    <div class="modal-content">
        <span class="close-btn">×</span>
        <h2><i class="fas fa-edit"></i> Edit Category</h2>
        <form id="editCategoryForm" action="${pageContext.request.contextPath}/admin/food-items" method="post">
            <input type="hidden" name="action" value="updateCategory">
            <div class="form-group">
                <label for="editCategoryId"><i class="fas fa-id-badge"></i> Category ID:</label>
                <input type="text" id="editCategoryId" name="categoryId" readonly>
            </div>
            <div class="form-group">
                <label for="editCategoryName"><i class="fas fa-tag"></i> Name:</label>
                <input type="text" id="editCategoryName" name="categoryName" required>
            </div>
            <div class="form-group">
                <label for="editCategoryDescription"><i class="fas fa-info-circle"></i> Description:</label>
                <textarea id="editCategoryDescription" name="categoryDescription" rows="3"></textarea>
            </div>
            <div class="form-buttons">
                <input type="submit" value="Update" class="apply-filter"><i class="fas fa-save"></i>
                <button type="button" class="reset-filter close-btn"><i class="fas fa-times"></i> Cancel</button>
            </div>
        </form>
    </div>
</div>

<!-- Delete Confirmation Modal -->
<div id="deleteModal" class="delete-modal">
    <div class="delete-modal-content">
        <span class="delete-modal-close">×</span>
        <h2><i class="fas fa-exclamation-triangle"></i> Confirm Deletion</h2>
        <p id="deleteModalMessage">Are you sure you want to delete this item?</p>
        <div class="delete-modal-buttons">
            <button class="delete-confirm-btn"><i class="fas fa-check"></i> Confirm</button>
            <button class="delete-cancel-btn"><i class="fas fa-times"></i> Cancel</button>
        </div>
    </div>
</div>

<script>
    // Handle Edit Food Item Button Click
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const modal = document.getElementById('editModal');
            const form = document.getElementById('editForm');

            // Populate form fields with data attributes
            document.getElementById('editFoodId').value = this.dataset.foodId;
            document.getElementById('editName').value = this.dataset.name;
            document.getElementById('editDescription').value = this.dataset.description;
            document.getElementById('editPrice').value = parseFloat(this.dataset.price).toFixed(2);
            document.getElementById('editFoodCategoryId').value = this.dataset.categoryId;
            document.getElementById('editAvailability').checked = this.dataset.available === 'true';

            modal.style.display = 'block';
        });
    });

    // Handle Edit Category Button Click
    document.querySelectorAll('.edit-category-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const modal = document.getElementById('editCategoryModal');
            const form = document.getElementById('editCategoryForm');

            // Populate form fields with data attributes
            document.getElementById('editCategoryId').value = this.dataset.categoryId;
            document.getElementById('editCategoryName').value = this.dataset.name;
            document.getElementById('editCategoryDescription').value = this.dataset.description;

            modal.style.display = 'block';
        });
    });

    // Handle Modal Close
    document.querySelectorAll('.close-btn, .delete-modal-close').forEach(button => {
        button.addEventListener('click', function() {
            document.getElementById('editModal').style.display = 'none';
            document.getElementById('editCategoryModal').style.display = 'none';
            document.getElementById('deleteModal').style.display = 'none';
        });
    });

    // Close Modals on Outside Click
    window.addEventListener('click', function(e) {
        const foodModal = document.getElementById('editModal');
        const categoryModal = document.getElementById('editCategoryModal');
        const deleteModal = document.getElementById('deleteModal');
        if (e.target === foodModal) {
            foodModal.style.display = 'none';
        }
        if (e.target === categoryModal) {
            categoryModal.style.display = 'none';
        }
        if (e.target === deleteModal) {
            deleteModal.style.display = 'none';
        }
    });

    // Handle Delete Button Click
    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const modal = document.getElementById('deleteModal');
            const message = document.getElementById('deleteModalMessage');
            const confirmBtn = document.querySelector('.delete-confirm-btn');

            // Populate modal with item name and deletion URL
            const itemName = this.dataset.itemName;
            const deleteUrl = this.dataset.deleteUrl;
            message.textContent = `Are you sure you want to delete ${itemName}?`;
            confirmBtn.dataset.deleteUrl = deleteUrl;

            modal.style.display = 'block';
        });
    });

    // Handle Delete Confirm Button
    document.querySelector('.delete-confirm-btn').addEventListener('click', function() {
        const deleteUrl = this.dataset.deleteUrl;
        if (deleteUrl) {
            window.location.href = deleteUrl;
        }
        document.getElementById('deleteModal').style.display = 'none';
    });

    // Handle Delete Cancel Button
    document.querySelector('.delete-cancel-btn').addEventListener('click', function() {
        document.getElementById('deleteModal').style.display = 'none';
    });
</script>
</body>
</html>