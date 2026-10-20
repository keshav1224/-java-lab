package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final AddressDAO addressDAO = new AddressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            req.setAttribute("addresses", addressDAO.findByUser(u.getUserId()));
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            Address a = new Address();
            a.setUserId(u.getUserId());
            a.setAddressLine(req.getParameter("addressLine"));
            a.setCity(req.getParameter("city"));
            a.setState(req.getParameter("state"));
            a.setPincode(req.getParameter("pincode"));
            a.setAddressType(req.getParameter("addressType"));
            addressDAO.insert(a);
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
