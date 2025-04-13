<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<%@ taglib uri="jakarta.tags.core" prefix="c" %>--%>
<%--<%@ page import="java.lang.Math" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--    <meta charset="UTF-8">--%>
<%--    <meta name="viewport" content="width=device-width, initial-scale=1.0">--%>
<%--    <title>Food Store</title>--%>
<%--    <!-- Google Fonts (Poppins) -->--%>
<%--    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">--%>
<%--    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">--%>
<%--    <!-- Font Awesome for Icons -->--%>
<%--    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">--%>
<%--    <link rel="stylesheet" href="css/styles.css">--%>
<%--</head>--%>
<%--<body>--%>
<%--<!-- Toast Notification Container -->--%>
<%--<div id="toast" class="toast"></div>--%>
<%--<div class="container">--%>
<%--    <!-- Filter Sidebar -->--%>
<%--    <div class="sidebar">--%>
<%--        <h3>FILTERS <i class="fas fa-filter"></i></h3>--%>
<%--        <form id="filterForm" action="food-items" method="get">--%>
<%--            <div class="filter-section">--%>
<%--                <h4>Categories</h4>--%>
<%--                <label><input type="checkbox" name="categories" value="Snacks" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Snacks'}">checked</c:if></c:forEach></c:if>> Snacks</label><br>--%>
<%--                <label><input type="checkbox" name="categories" value="Beverages" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Beverages'}">checked</c:if></c:forEach></c:if>> Beverages</label><br>--%>
<%--                <label><input type="checkbox" name="categories" value="Desserts" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Desserts'}">checked</c:if></c:forEach></c:if>> Desserts</label><br>--%>
<%--                <label><input type="checkbox" name="categories" value="Main Course" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Main Course'}">checked</c:if></c:forEach></c:if>> Main Course</label><br>--%>
<%--                <label><input type="checkbox" name="categories" value="Sandwiches" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Sandwiches'}">checked</c:if></c:forEach></c:if>> Sandwiches</label>--%>
<%--            </div>--%>
<%--            <div class="filter-section">--%>
<%--                <h4>PRICE</h4>--%>
<%--                <input type="range" min="50" max="500" value="${param.reset == 'true' ? '500' : (param.priceRange != null && param.priceRange != '' ? param.priceRange : '500')}" name="priceRange" id="priceRange" oninput="showPriceValue(this)">--%>
<%--                <div class="price-range">--%>
<%--                    <span>50 LKR</span>--%>
<%--                    <span>500 LKR</span>--%>
<%--                </div>--%>
<%--                <div id="priceTooltip" style="display: none; position: absolute; background: #333; color: #fff; padding: 5px 10px; border-radius: 4px; font-size: 12px;"></div>--%>
<%--            </div>--%>
<%--            <div class="filter-buttons">--%>
<%--                <button type="submit" class="apply-filter">Apply Filter <i class="fas fa-check"></i></button>--%>
<%--                <button type="submit" class="reset-filter" name="reset" value="true">Reset Filters <i class="fas fa-undo"></i></button>--%>
<%--            </div>--%>
<%--            <input type="hidden" name="page" value="${currentPage != null ? currentPage : '1'}">--%>
<%--            <input type="hidden" name="sortBy" value="${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}">--%>
<%--        </form>--%>
<%--    </div>--%>

<%--    <!-- Main Content -->--%>
<%--    <div class="main-content">--%>
<%--        <div class="header">--%>
<%--            <h2>Food Items</h2>--%>
<%--            <div class="sort">--%>
<%--                <span>--%>
<%--                    Showing--%>
<%--                    ${(currentPage - 1) * itemsPerPage + 1} ---%>
<%--                    ${currentPage * itemsPerPage > totalItems ? totalItems : currentPage * itemsPerPage}--%>
<%--                    of ${totalItems} Products--%>
<%--                </span>--%>
<%--                <form id="sortForm" action="food-items" method="get">--%>
<%--                    <select name="sortBy" onchange="document.getElementById('sortForm').submit()">--%>
<%--                        <option value="lowToHigh" ${param.sortBy == 'lowToHigh' ? 'selected' : ''}>Sort by: Price Low to High</option>--%>
<%--                        <option value="highToLow" ${param.sortBy == null || param.sortBy == '' || param.sortBy == 'highToLow' ? 'selected' : ''}>Sort by: Price High to Low</option>--%>
<%--                    </select>--%>
<%--                    <!-- Preserve filter parameters -->--%>
<%--                    <c:forEach var="cat" items="${paramValues.categories}">--%>
<%--                        <input type="hidden" name="categories" value="${cat}">--%>
<%--                    </c:forEach>--%>
<%--                    <input type="hidden" name="priceRange" value="${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}">--%>
<%--                    <input type="hidden" name="page" value="${currentPage != null ? currentPage : '1'}">--%>
<%--                </form>--%>
<%--            </div>--%>
<%--        </div>--%>

<%--        <!-- Product Grid -->--%>
<%--        <div class="product-grid">--%>
<%--            <c:if test="${empty foodItems}">--%>
<%--                <p>No food items available.</p>--%>
<%--            </c:if>--%>
<%--            <c:forEach var="item" items="${foodItems}">--%>
<%--                <div class="product-card">--%>
<%--                    <img src="images/${item.foodId}.avif" alt="${item.name}">--%>
<%--                    <h3>${item.name}</h3>--%>
<%--                    <div class="rating">--%>
<%--                        <c:forEach begin="1" end="5" var="i">--%>
<%--                            <i class="${i <= Math.min(5.0, item.orderCount / 10.0) ? 'fas fa-star' : 'far fa-star'}"></i>--%>
<%--                        </c:forEach>--%>
<%--                        <span>${item.orderCount > 0 ? String.format('%.1f', Math.min(5.0, item.orderCount / 10.0)) : '4.0'}/5</span>--%>
<%--                    </div>--%>
<%--                    <p class="price">${String.format("%.2f", item.price)} LKR</p>--%>
<%--                    <div class="buttons">--%>
<%--                        <button class="order-now">Order Now <i class="fas fa-shopping-bag"></i></button>--%>
<%--                        <button class="add-to-cart">Add to Cart <i class="fas fa-cart-plus"></i></button>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </c:forEach>--%>
<%--        </div>--%>

<%--        <!-- Pagination -->--%>
<%--        <div class="pagination">--%>
<%--            <c:if test="${currentPage > 1}">--%>
<%--                <a href="food-items?page=${currentPage - 1}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">--%>
<%--                    <button class="pagination-btn"><i class="fas fa-chevron-left"></i> Previous</button>--%>
<%--                </a>--%>
<%--            </c:if>--%>
<%--            <c:forEach begin="1" end="${totalPages}" var="i">--%>
<%--                <a href="food-items?page=${i}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">--%>
<%--                    <span class="pagination-number ${i == currentPage ? 'active' : ''}">${i}</span>--%>
<%--                </a>--%>
<%--            </c:forEach>--%>
<%--            <c:if test="${currentPage < totalPages}">--%>
<%--                <a href="food-items?page=${currentPage + 1}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">--%>
<%--                    <button class="pagination-btn">Next <i class="fas fa-chevron-right"></i></button>--%>
<%--                </a>--%>
<%--            </c:if>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<%--<script>--%>
<%--    function showPriceValue(slider) {--%>
<%--        const tooltip = document.getElementById('priceTooltip');--%>
<%--        const value = slider.value;--%>
<%--        tooltip.textContent = value + ' LKR';--%>
<%--        tooltip.style.display = 'block';--%>
<%--        // Position tooltip above the slider thumb--%>
<%--        const rect = slider.getBoundingClientRect();--%>
<%--        const thumbWidth = 16; // Approximate thumb width--%>
<%--        const percent = (value - slider.min) / (slider.max - slider.min);--%>
<%--        const thumbPosition = percent * (rect.width - thumbWidth);--%>
<%--        tooltip.style.left = (rect.left + thumbPosition + thumbWidth / 2 - 30) + 'px';--%>
<%--        tooltip.style.top = (rect.top - 30) + 'px';--%>
<%--    }--%>
<%--    // Hide tooltip when not interacting--%>
<%--    document.getElementById('priceRange').addEventListener('change', function() {--%>
<%--        document.getElementById('priceTooltip').style.display = 'none';--%>
<%--    });--%>

<%--    // New script for Order Now and Add to Cart buttons--%>
<%--    document.querySelectorAll('.order-now, .add-to-cart').forEach(button => {--%>
<%--        button.addEventListener('click', function() {--%>
<%--            // Pulse animation on the button--%>
<%--            this.classList.add('pulse');--%>

<%--            // Bounce animation on the icon--%>
<%--            const icon = this.querySelector('i');--%>
<%--            if (icon) {--%>
<%--                icon.classList.add('bounce');--%>
<%--            }--%>

<%--            // Show toast notification--%>
<%--            const toast = document.getElementById('toast');--%>
<%--            const itemName = this.getAttribute('data-item-name');--%>
<%--            const action = this.classList.contains('order-now') ? 'Order Placed!' : 'Added to Cart!';--%>
<%--            toast.textContent = `${action} (${itemName})`;--%>
<%--            toast.classList.add('show');--%>

<%--            // Hide toast after 2 seconds--%>
<%--            setTimeout(() => {--%>
<%--                toast.classList.remove('show');--%>
<%--            }, 2000);--%>
<%--        });--%>
<%--    });--%>
<%--</script>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.lang.Math" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food Store</title>
    <!-- Google Fonts (Poppins) -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Patua+One:wght@400&display=swap">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;700&display=swap">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<%@ include file="header.jsp" %>
<!-- Toast Notification Container -->
<div id="toast" class="toast"></div>
<div class="container">
    <!-- Filter Sidebar -->
    <div class="sidebar">
        <h3>FILTERS <i class="fas fa-filter"></i></h3>
        <form id="filterForm" action="food-items" method="get">
            <div class="filter-section">
                <h4>Categories</h4>
                <label><input type="checkbox" name="categories" value="Snacks" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Snacks'}">checked</c:if></c:forEach></c:if>> Snacks</label><br>
                <label><input type="checkbox" name="categories" value="Beverages" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Beverages'}">checked</c:if></c:forEach></c:if>> Beverages</label><br>
                <label><input type="checkbox" name="categories" value="Desserts" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Desserts'}">checked</c:if></c:forEach></c:if>> Desserts</label><br>
                <label><input type="checkbox" name="categories" value="Main Course" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Main Course'}">checked</c:if></c:forEach></c:if>> Main Course</label><br>
                <label><input type="checkbox" name="categories" value="Sandwiches" <c:if test="${param.reset != 'true'}"><c:forEach var="cat" items="${paramValues.categories}"><c:if test="${cat == 'Sandwiches'}">checked</c:if></c:forEach></c:if>> Sandwiches</label>
            </div>
            <div class="filter-section">
                <h4>PRICE</h4>
                <input type="range" min="50" max="500" value="${param.reset == 'true' ? '500' : (param.priceRange != null && param.priceRange != '' ? param.priceRange : '500')}" name="priceRange" id="priceRange" oninput="showPriceValue(this)">
                <div class="price-range">
                    <span>50 LKR</span>
                    <span>500 LKR</span>
                </div>
                <div id="priceTooltip" style="display: none; position: absolute; background: #333; color: #fff; padding: 5px 10px; border-radius: 4px; font-size: 12px;"></div>
            </div>
            <div class="filter-buttons">
                <button type="submit" class="apply-filter">Apply Filter <i class="fas fa-check"></i></button>
                <button type="submit" class="reset-filter" name="reset" value="true">Reset Filters <i class="fas fa-undo"></i></button>
            </div>
            <input type="hidden" name="page" value="${currentPage != null ? currentPage : '1'}">
            <input type="hidden" name="sortBy" value="${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}">
        </form>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="header">
            <h2>Food Items</h2>
            <div class="sort">
                <span>
                    Showing
                    ${(currentPage - 1) * itemsPerPage + 1} -
                    ${currentPage * itemsPerPage > totalItems ? totalItems : currentPage * itemsPerPage}
                    of ${totalItems} Products
                </span>
                <form id="sortForm" action="food-items" method="get">
                    <select name="sortBy" onchange="document.getElementById('sortForm').submit()">
                        <option value="lowToHigh" ${param.sortBy == 'lowToHigh' ? 'selected' : ''}>Sort by: Price Low to High</option>
                        <option value="highToLow" ${param.sortBy == null || param.sortBy == '' || param.sortBy == 'highToLow' ? 'selected' : ''}>Sort by: Price High to Low</option>
                    </select>
                    <!-- Preserve filter parameters -->
                    <c:forEach var="cat" items="${paramValues.categories}">
                        <input type="hidden" name="categories" value="${cat}">
                    </c:forEach>
                    <input type="hidden" name="priceRange" value="${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}">
                    <input type="hidden" name="page" value="${currentPage != null ? currentPage : '1'}">
                </form>
            </div>
        </div>

        <!-- Product Grid -->
        <div class="product-grid">
            <c:if test="${empty foodItems}">
                <p>No food items available.</p>
            </c:if>
            <c:forEach var="item" items="${foodItems}">
                <div class="product-card">
                    <img src="images/${item.foodId}.avif" alt="${item.name}">
                    <h3>${item.name}</h3>
                    <div class="rating">
                        <c:forEach begin="1" end="5" var="i">
                            <i class="${i <= Math.min(5.0, item.orderCount / 10.0) ? 'fas fa-star' : 'far fa-star'}"></i>
                        </c:forEach>
                        <span>${item.orderCount > 0 ? String.format('%.1f', Math.min(5.0, item.orderCount / 10.0)) : '4.0'}/5</span>
                    </div>
                    <p class="price">${String.format("%.2f", item.price)} LKR</p>
                    <div class="buttons">
                        <button class="order-now" data-item-name="${item.name}">Order Now <i class="fas fa-shopping-bag"></i></button>
                        <button class="add-to-cart" data-item-name="${item.name}">Add to Cart <i class="fas fa-cart-plus"></i></button>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="food-items?page=${currentPage - 1}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">
                    <button class="pagination-btn"><i class="fas fa-chevron-left"></i> Previous</button>
                </a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="food-items?page=${i}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">
                    <span class="pagination-number ${i == currentPage ? 'active' : ''}">${i}</span>
                </a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="food-items?page=${currentPage + 1}<c:forEach var="cat" items="${paramValues.categories}">&categories=${cat}</c:forEach>&priceRange=${param.priceRange != null && param.priceRange != '' ? param.priceRange : '500'}&sortBy=${param.sortBy != null && param.sortBy != '' ? param.sortBy : 'highToLow'}" class="pagination-link">
                    <button class="pagination-btn">Next <i class="fas fa-chevron-right"></i></button>
                </a>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>

<script>
    // Price slider script (unchanged)
    // function showPriceValue(slider) {
    //     const tooltip = document.getElementById('priceTooltip');
    //     const value = slider.value;
    //     tooltip.textContent = value + ' LKR';
    //     tooltip.style.display = 'block';
    //     const rect = slider.getBoundingClientRect();
    //     const thumbWidth = 16;
    //     const percent = (value - slider.min) / (slider.max - slider.min);
    //     const thumbPosition = percent * (rect.width - thumbWidth);
    //     tooltip.style.left = (rect.left + thumbPosition + thumbWidth / 2 - 30) + 'px';
    //     tooltip.style.top = (rect.top - 30) + 'px';
    // }

    // Debounce utility to limit how often a function is called
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    function showPriceValue(slider) {
        const tooltip = document.getElementById('priceTooltip');
        const value = slider.value;
        tooltip.textContent = value + ' LKR';
        tooltip.style.display = 'block';
        const rect = slider.getBoundingClientRect();
        const thumbWidth = 16; // Approximate thumb width
        const percent = (value - slider.min) / (slider.max - slider.min);
        const thumbPosition = percent * (rect.width - thumbWidth);
        tooltip.style.left = (rect.left + thumbPosition + thumbWidth / 2 - 30) + 'px';
        tooltip.style.top = (rect.top + window.scrollY - 30) + 'px';
    }

    function hidePriceTooltip() {
        const tooltip = document.getElementById('priceTooltip');
        tooltip.style.display = 'none';
    }

    // Attach debounced handler to input event
    const priceRange = document.getElementById('priceRange');
    const debouncedShowPriceValue = debounce(showPriceValue, 50); // 50ms debounce
    priceRange.addEventListener('input', () => debouncedShowPriceValue(priceRange));

    // Hide tooltip on various interaction endings
    ['change', 'mouseup', 'touchend'].forEach(event => {
        priceRange.addEventListener(event, hidePriceTooltip);
    });

    // Fallback: Hide tooltip after 3 seconds of inactivity
    let hideTooltipTimeout;
    priceRange.addEventListener('input', () => {
        clearTimeout(hideTooltipTimeout);
        hideTooltipTimeout = setTimeout(hidePriceTooltip, 3000);
    });

    // Updated script for Order Now and Add to Cart buttons
    document.querySelectorAll('.order-now, .add-to-cart').forEach(button => {
        button.addEventListener('click', function() {
            // Remove and re-add pulse animation on the button
            this.classList.remove('pulse');
            this.classList.add('pulse');

            // Remove and re-add bounce animation on the icon with a reflow
            const icon = this.querySelector('i');
            if (icon) {
                icon.classList.remove('bounce');
                // Force reflow to restart the animation
                void icon.offsetWidth;
                icon.classList.add('bounce');
            }

            // Show toast notification only for Add to Cart
            if (this.classList.contains('add-to-cart')) {
                const toast = document.getElementById('toast');
                toast.textContent = "Added to Cart!"; // Simplified message
                toast.classList.add('show');

                // Hide toast after 2 seconds
                setTimeout(() => {
                    toast.classList.remove('show');
                }, 2000);
            }
        });
    });
</script>
</body>
</html>