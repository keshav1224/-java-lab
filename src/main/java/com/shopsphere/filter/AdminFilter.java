package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith("/admin/login")) { chain.doFilter(request, response); return; }

        HttpSession s = req.getSession(false);
        User u = s == null ? null : (User) s.getAttribute("user");
        if (u == null || !"ADMIN".equalsIgnoreCase(u.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
