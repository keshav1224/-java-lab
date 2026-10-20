package com.shopsphere.swing.view;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.shopsphere.swing.db.DB;
import com.shopsphere.swing.model.AdminUser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passField = new JPasswordField(20);

    public LoginFrame() {
        setTitle("ShopSphere — Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(380, 220);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(loginBtn, gbc);

        loginBtn.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginBtn);
    }

    private void doLogin() {
        String email = emailField.getText().trim();
        String pass = new String(passField.getPassword());
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE email=? AND role='ADMIN'")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("password");
                    if (BCrypt.verifyer().verify(pass.toCharArray(), hash).verified) {
                        AdminUser.userId = rs.getInt("user_id");
                        AdminUser.name = rs.getString("name");
                        AdminUser.role = rs.getString("role");
                        new AdminDashboard().setVisible(true);
                        dispose();
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid admin credentials", "Login failed", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
