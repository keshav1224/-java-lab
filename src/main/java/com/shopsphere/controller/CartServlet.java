package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            var items = cartService.list(u.getUserId());
            double subtotal = items.stream().mapToDouble(ci -> ci.getSubtotal()).sum();
            req.setAttribute("items", items);
            req.setAttribute("subtotal", subtotal);
            req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        String action = req.getParameter("action");
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            if ("add".equals(action)) {
                int qty = req.getParameter("qty") != null ? Integer.parseInt(req.getParameter("qty")) : 1;
                cartService.add(u.getUserId(), productId, qty);
            } else if ("update".equals(action)) {
                int qty = Integer.parseInt(req.getParameter("qty"));
                cartService.update(u.getUserId(), productId, qty);
            } else if ("remove".equals(action)) {
                cartService.remove(u.getUserId(), productId);
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
