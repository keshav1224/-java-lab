package com.shopsphere.swing.view;

import com.shopsphere.swing.db.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventoryFrame extends JFrame {
    public InventoryFrame() {
        setTitle("Low Stock Inventory (≤ 15)");
        setSize(600, 380);
        setLocationRelativeTo(null);
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Product","Brand","Stock","Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT product_id,name,brand,stock,status FROM products WHERE stock <= 15 ORDER BY stock");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getBoolean(5) ? "Active" : "Inactive"});
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
