package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws SQLException {
        User u = userDAO.findByEmail(email);
        if (u == null || !u.isStatus()) return null;
        if (!PasswordUtil.verify(password, u.getPassword())) return null;
        return u;
    }

    public User register(String name, String email, String password, String mobile) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already registered");
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(PasswordUtil.hash(password));
        u.setMobile(mobile);
        u.setRole("CUSTOMER");
        u.setStatus(true);
        int id = userDAO.insert(u);
        u.setUserId(id);
        u.setPassword(null);
        return u;
    }
}
