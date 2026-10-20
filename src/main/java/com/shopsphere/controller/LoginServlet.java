package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/admin/login"})
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.startsWith("/admin")) {
            req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String path = req.getServletPath();
        boolean adminLogin = path.startsWith("/admin");

        try {
            User u = authService.login(email, password);
            if (u == null) {
                req.setAttribute("error", "Invalid email or password");
                if (adminLogin) req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
                else req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }
            if (adminLogin && !"ADMIN".equalsIgnoreCase(u.getRole())) {
                req.setAttribute("error", "This account is not an administrator");
                req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("user", u);
            session.setAttribute("role", u.getRole());
            session.setMaxInactiveInterval(30 * 60);

            if (adminLogin) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                String next = req.getParameter("next");
                resp.sendRedirect(req.getContextPath() + (next != null && !next.isBlank() ? next : "/"));
            }
        } catch (Exception e) {
            req.setAttribute("error", "Login error: " + e.getMessage());
            if (adminLogin) req.getRequestDispatcher("/WEB-INF/views/admin/login.jsp").forward(req, resp);
            else req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}
