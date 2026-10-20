package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            req.setAttribute("wishlist", wishlistDAO.findByUser(u.getUserId()));
            req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            String action = req.getParameter("action");
            if ("remove".equals(action)) wishlistDAO.remove(u.getUserId(), productId);
            else wishlistDAO.add(u.getUserId(), productId);
            String from = req.getParameter("from");
            resp.sendRedirect(req.getContextPath() + (from != null ? from : "/wishlist"));
        } catch (Exception e) { throw new ServletException(e); }
    }
}
