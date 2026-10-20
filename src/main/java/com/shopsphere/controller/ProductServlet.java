package com.shopsphere.controller;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Product;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/products", "/product-details"})
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/product-details".equals(path)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product p = productService.get(id);
                if (p == null) { resp.sendError(404); return; }

                var reviewDao = new com.shopsphere.dao.ReviewDAO();
                req.setAttribute("product", p);
                req.setAttribute("reviews", reviewDao.findByProduct(id));
                req.setAttribute("avgRating", reviewDao.averageRating(id));
                req.setAttribute("reviewCount", reviewDao.countByProduct(id));
                req.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(req, resp);
                return;
            }

            String keyword = req.getParameter("q");
            String catParam = req.getParameter("category");
            Integer catId = (catParam != null && !catParam.isBlank()) ? Integer.parseInt(catParam) : null;
            String sort = req.getParameter("sort");

            req.setAttribute("products", productService.search(keyword, catId, sort));
            req.setAttribute("categories", categoryDAO.findAll());
            req.setAttribute("selectedCategory", catId);
            req.setAttribute("q", keyword);
            req.setAttribute("sort", sort);

            var u = SessionUtil.currentUser(req);
            if (u != null) req.setAttribute("cartCount", cartService.count(u.getUserId()));
            req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
