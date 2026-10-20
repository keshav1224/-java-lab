package com.shopsphere.util;

import com.shopsphere.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class SessionUtil {
    public static final String USER_KEY = "user";
    private SessionUtil() {}

    public static User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (User) s.getAttribute(USER_KEY);
    }

    public static boolean isAdmin(HttpServletRequest req) {
        User u = currentUser(req);
        return u != null && "ADMIN".equalsIgnoreCase(u.getRole());
    }
}
