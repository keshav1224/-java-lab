package com.shopsphere.swing.view;

import com.shopsphere.swing.db.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerFrame extends JFrame {
    public CustomerFrame() {
        setTitle("Customers");
        setSize(700, 420);
        setLocationRelativeTo(null);
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Email","Mobile","Role","Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT user_id,name,email,mobile,role,status FROM users ORDER BY user_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getBoolean(6)});
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
