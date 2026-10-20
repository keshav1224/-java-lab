package com.shopsphere.filter;

import com.shopsphere.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Set;

@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final Set<String> PUBLIC = Set.of(
            "/", "/index.jsp", "/login", "/register", "/logout",
            "/products", "/product-details", "/search", "/admin/login",
            "/css/", "/js/", "/images/", "/error", "/favicon.ico"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Admin login page is public
        if (path.startsWith("/admin/login")) { chain.doFilter(request, response); return; }

        // Public endpoints
        if (path.isEmpty() || PUBLIC.stream().anyMatch(p -> path.equals(p) || path.startsWith(p))) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User u = session == null ? null : (User) session.getAttribute("user");
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/login?next=" + java.net.URLEncoder.encode(path, "UTF-8"));
            return;
        }
        chain.doFilter(request, response);
    }
}
