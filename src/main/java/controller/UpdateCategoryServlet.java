package java.controller;

import java.model.Category;
import java.service.CategoryService;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/updateCategory")
public class UpdateCategoryServlet extends HttpServlet {
    private CategoryService categoryService = new CategoryService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoryId = request.getParameter("categoryId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Category category = new Category(categoryId, name, description);
        categoryService.updateCategory(category);

        response.sendRedirect("listCategories.jsp");
    }
}