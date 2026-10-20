package com.shopsphere.controller.admin;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {
    private final CategoryDAO dao = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("edit".equals(action) || "new".equals(action)) {
                if ("edit".equals(action)) req.setAttribute("category", dao.findById(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("/WEB-INF/views/admin/category-form.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("categories", dao.findAllIncludingInactive());
            req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(req.getParameter("id")));
            } else {
                Category cat = new Category();
                String idStr = req.getParameter("categoryId");
                if (idStr != null && !idStr.isBlank()) cat.setCategoryId(Integer.parseInt(idStr));
                cat.setName(req.getParameter("name"));
                cat.setDescription(req.getParameter("description"));
                cat.setStatus("on".equals(req.getParameter("status")));
                if (cat.getCategoryId() > 0) dao.update(cat);
                else dao.insert(cat);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
