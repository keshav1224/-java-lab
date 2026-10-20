package com.shopsphere.controller.admin;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("productCount", productDAO.countAll());
            req.setAttribute("orderCount", orderDAO.countAll());
            req.setAttribute("revenue", orderDAO.totalRevenue());
            req.setAttribute("lowStock", productDAO.findLowStock(15));
            req.setAttribute("recentOrders", orderDAO.findAll().stream().limit(10).toList());
            req.setAttribute("customerCount", userDAO.findAll().size());
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
