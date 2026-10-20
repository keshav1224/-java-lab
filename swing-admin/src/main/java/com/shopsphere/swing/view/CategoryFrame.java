package com.shopsphere.swing.view;

import com.shopsphere.swing.db.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CategoryFrame extends JFrame {
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Name","Description","Active"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);

    public CategoryFrame() {
        setTitle("Categories");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel buttons = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add");
        buttons.add(refresh); buttons.add(add);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        refresh.addActionListener(e -> load());
        add.addActionListener(e -> addCategory());
        load();
    }

    private void load() {
        model.setRowCount(0);
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM categories ORDER BY category_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)});
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void addCategory() {
        JTextField name = new JTextField();
        JTextArea desc = new JTextArea(3, 20);
        JPanel p = new JPanel(new GridLayout(0, 1, 6, 6));
        p.add(new JLabel("Name:")); p.add(name);
        p.add(new JLabel("Description:")); p.add(new JScrollPane(desc));
        if (JOptionPane.showConfirmDialog(this, p, "Add Category", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO categories(name,description,status) VALUES(?,?,1)")) {
            ps.setString(1, name.getText());
            ps.setString(2, desc.getText());
            ps.executeUpdate();
            load();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
