package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.OrderItemDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            List<Order> orders = orderDAO.findByUser(u.getUserId());
            for (Order o : orders) o.setItems(itemDAO.findByOrder(o.getOrderId()));
            req.setAttribute("orders", orders);
            req.setAttribute("placedId", req.getParameter("placed"));
            req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
