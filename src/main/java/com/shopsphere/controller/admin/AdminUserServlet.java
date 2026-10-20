package com.shopsphere.controller.admin;

import com.shopsphere.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/customers")
public class AdminUserServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("customers", dao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/customers.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
