package com.shopsphere.controller.admin;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.OrderItemDAO;
import com.shopsphere.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if ("view".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Order o = orderDAO.findById(id);
                if (o != null) o.setItems(itemDAO.findByOrder(id));
                req.setAttribute("order", o);
                req.getRequestDispatcher("/WEB-INF/views/admin/order-detail.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("orders", orderDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String status = req.getParameter("status");
            orderDAO.updateStatus(orderId, status);
            resp.sendRedirect(req.getContextPath() + "/admin/orders?action=view&id=" + orderId);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
