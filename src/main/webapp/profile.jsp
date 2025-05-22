<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile</title>
    <link rel="icon" type="image/x-icon" href="external/letter-q.png">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One&display=swap">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>
<%@ include file="header.jsp" %>

<!-- Toast Notification -->
<div id="toast" class="toast"></div>

<div class="container">
    <div class="main-content">
        <div class="profile-card">
            <div class="profile-header">
                <h2><i class="fas fa-user"></i> My Profile</h2>
            </div>
            <!-- Profile Details -->
            <div class="profile-details">
                <div>
                    <label>Name:</label>
                    <p>${sessionScope.user.name}</p>
                </div>
                <div>
                    <label>Email:</label>
                    <p>${sessionScope.user.email}</p>
                </div>
                <div>
                    <label>Phone:</label>
                    <p>${sessionScope.user.phoneNumber}</p>
                </div>
                <div>
                    <label>Address:</label>
                    <p>${sessionScope.user.address}</p>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="profile-buttons">
                <button id="editBtn" class="start-ordering">
                    <i class="fas fa-edit"></i> Edit Profile
                </button>
                <button id="deleteBtn" class="start-ordering delete-btn" data-item-name="your account" data-delete-url="${pageContext.request.contextPath}/user">
                    <i class="fas fa-trash"></i> Delete Account
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Modal for Editing Profile -->
<div id="editModal" class="modal">
    <div class="modal-content">
        <h3>Edit Profile</h3>
        <form id="editForm" action="${pageContext.request.contextPath}/user" method="post">
            <input type="hidden" name="action" value="update">
            <div class="modal-field">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name" value="${sessionScope.user.name}" required>
                <span id="nameError" class="error"></span>
            </div>
            <div class="modal-field">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${sessionScope.user.email}" required>
                <span id="emailError" class="error"></span>
            </div>
            <div class="modal-field">
                <label for="phone">Phone:</label>
                <input type="tel" id="phone" name="phone" value="${sessionScope.user.phoneNumber}" pattern="[0-9]{10}" required placeholder="1234567890">
                <span id="phoneError" class="error"></span>
            </div>
            <div class="modal-field">
                <label for="address">Address:</label>
                <textarea id="address" name="address" required>${sessionScope.user.address}</textarea>
                <span id="addressError" class="error"></span>
            </div>
            <div class="modal-buttons">
                <button type="button" id="cancelBtn" class="cancel-btn">Cancel</button>
                <button type="submit" class="start-ordering">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal for Delete Confirmation -->
<div id="deleteModal" class="modal">
    <div class="modal-content">
        <h3>Confirm Deletion</h3>
        <p id="deleteModalMessage"></p>
        <div class="modal-buttons">
            <button type="button" class="cancel-btn">Cancel</button>
            <button type="button" class="start-ordering delete-confirm-btn">Confirm</button>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

<script>
    // Modal Control
    const editBtn = document.getElementById('editBtn');
    const editModal = document.getElementById('editModal');
    const cancelBtn = document.getElementById('cancelBtn');

    editBtn.addEventListener('click', () => {
        editModal.style.display = 'flex';
    });

    cancelBtn.addEventListener('click', () => {
        editModal.style.display = 'none';
        resetForm();
    });

    // Form Validation
    const editForm = document.getElementById('editForm');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const addressInput = document.getElementById('address');
    const nameError = document.getElementById('nameError');
    const emailError = document.getElementById('emailError');
    const phoneError = document.getElementById('phoneError');
    const addressError = document.getElementById('addressError');

    function validateForm() {
        let isValid = true;

        // Name validation
        if (nameInput.value.length < 2) {
            nameError.textContent = 'Name must be at least 2 characters.';
            isValid = false;
        } else {
            nameError.textContent = '';
        }

        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailInput.value)) {
            emailError.textContent = 'Please enter a valid email.';
            isValid = false;
        } else {
            emailError.textContent = '';
        }

        // Phone validation
        const phoneRegex = /^[0-9]{10}$/;
        if (!phoneRegex.test(phoneInput.value)) {
            phoneError.textContent = 'Phone must be 10 digits.';
            isValid = false;
        } else {
            phoneError.textContent = '';
        }

        // Address validation
        if (addressInput.value.trim().length < 5) {
            addressError.textContent = 'Address must be at least 5 characters.';
            isValid = false;
        } else {
            addressError.textContent = '';
        }

        return isValid;
    }

    [nameInput, emailInput, phoneInput, addressInput].forEach(input => {
        input.addEventListener('input', validateForm);
    });

    editForm.addEventListener('submit', (e) => {
        if (!validateForm()) {
            e.preventDefault();
            showToast('Please fix the errors before saving.', 'error');
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
            message.textContent = `Are you sure you want to delete ${itemName}? This action cannot be undone.`;
            confirmBtn.dataset.deleteUrl = deleteUrl;

            modal.style.display = 'flex';
        });
    });

    // Handle Delete Confirm Button
    document.querySelector('.delete-confirm-btn').addEventListener('click', function() {
        const deleteUrl = this.dataset.deleteUrl;
        if (deleteUrl) {
            // Send POST request for delete action
            fetch(deleteUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'action=delete'
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        showToast('Account deleted. Redirecting to login...', 'success');
                        setTimeout(() => window.location.href = '${pageContext.request.contextPath}/login.jsp', 2000);
                    } else {
                        showToast(data.error || 'Failed to delete account.', 'error');
                    }
                    document.getElementById('deleteModal').style.display = 'none';
                })
                .catch(error => {
                    showToast('Failed to delete account due to server error.', 'error');
                    document.getElementById('deleteModal').style.display = 'none';
                });
        }
    });

    // Handle Delete Cancel Button
    document.querySelector('.cancel-btn').addEventListener('click', function() {
        document.getElementById('deleteModal').style.display = 'none';
    });

    // Toast Notification
    function showToast(message, type) {
        const toast = document.getElementById('toast');
        toast.textContent = message;
        toast.classList.remove('success', 'error');
        toast.classList.add(type, 'show');
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    }

    function resetForm() {
        nameInput.value = '${sessionScope.user.name}';
        emailInput.value = '${sessionScope.user.email}';
        phoneInput.value = '${sessionScope.user.phoneNumber}';
        addressInput.value = '${sessionScope.user.address}';
        nameError.textContent = '';
        emailError.textContent = '';
        phoneError.textContent = '';
        addressError.textContent = '';
    }

    // Show Toast for Update/Delete
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showToast('Profile updated successfully!', 'success');
        } else if (urlParams.get('updated') === 'false') {
            showToast(urlParams.get('error') || 'Profile update failed.', 'error');
        } else if (urlParams.get('deleted') === 'true') {
            showToast('Account deleted. Redirecting to login...', 'success');
            setTimeout(() => window.location.href = '${pageContext.request.contextPath}/login.jsp', 2000);
        } else if (urlParams.get('deleted') === 'false') {
            showToast(urlParams.get('error') || 'Account deletion failed.', 'error');
        }
    };

    // Handle Delete Cancel Button - Fix
    document.querySelectorAll('.cancel-btn').forEach(button => {
        button.addEventListener('click', function() {
            const modal = this.closest('.modal');
            modal.style.display = 'none';
        });
    });
</script>
</body>
</html>