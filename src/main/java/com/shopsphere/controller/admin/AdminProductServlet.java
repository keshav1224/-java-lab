package com.shopsphere.controller.admin;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("edit".equals(action) || "new".equals(action)) {
                req.setAttribute("categories", categoryDAO.findAllIncludingInactive());
                if ("edit".equals(action)) {
                    int id = Integer.parseInt(req.getParameter("id"));
                    req.setAttribute("product", productService.get(id));
                }
                req.getRequestDispatcher("/WEB-INF/views/admin/product-form.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("products", productService.all());
            req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("delete".equals(action)) {
                productService.delete(Integer.parseInt(req.getParameter("id")));
            } else {
                Product p = new Product();
                String idStr = req.getParameter("productId");
                if (idStr != null && !idStr.isBlank()) p.setProductId(Integer.parseInt(idStr));
                p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
                p.setName(req.getParameter("name"));
                p.setBrand(req.getParameter("brand"));
                p.setDescription(req.getParameter("description"));
                p.setPrice(Double.parseDouble(req.getParameter("price")));
                p.setDiscount(req.getParameter("discount") == null || req.getParameter("discount").isBlank() ? 0 : Double.parseDouble(req.getParameter("discount")));
                p.setStock(Integer.parseInt(req.getParameter("stock")));
                p.setImageUrl(req.getParameter("imageUrl"));
                p.setStatus("on".equals(req.getParameter("status")));
                if (p.getProductId() > 0) productService.update(p);
                else productService.create(p);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
