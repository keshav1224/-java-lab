package com.shopsphere.swing.view;

import com.shopsphere.swing.db.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderFrame extends JFrame {
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Order ID","User","Total","Payment","Status","Created"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public OrderFrame() {
        setTitle("Orders");
        setSize(750, 430);
        setLocationRelativeTo(null);

        JPanel controls = new JPanel();
        JButton refresh = new JButton("Refresh");
        JComboBox<String> status = new JComboBox<>(new String[]{"PLACED","CONFIRMED","SHIPPED","DELIVERED","CANCELLED"});
        JButton update = new JButton("Update Status");
        controls.add(refresh); controls.add(new JLabel("Status:")); controls.add(status); controls.add(update);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        refresh.addActionListener(e -> load());
        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Select an order"); return; }
            int id = (Integer) model.getValueAt(r, 0);
            try (Connection c = DB.ds().getConnection();
                 PreparedStatement ps = c.prepareStatement("UPDATE orders SET order_status=? WHERE order_id=?")) {
                ps.setString(1, (String) status.getSelectedItem());
                ps.setInt(2, id);
                ps.executeUpdate();
                load();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        load();
    }

    private void load() {
        model.setRowCount(0);
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT order_id,user_id,total_amount,payment_method,order_status,created_at " +
                     "FROM orders ORDER BY order_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getDouble(3),
                        rs.getString(4), rs.getString(5), rs.getTimestamp(6)});
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
