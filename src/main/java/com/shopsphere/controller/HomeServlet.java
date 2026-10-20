package com.shopsphere.controller;

import com.shopsphere.service.CartService;
import com.shopsphere.service.ProductService;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"", "/"})
public class HomeServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("featured", productService.search(null, null, "discount"));
            var u = SessionUtil.currentUser(req);
            if (u != null) req.setAttribute("cartCount", cartService.count(u.getUserId()));
            req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
