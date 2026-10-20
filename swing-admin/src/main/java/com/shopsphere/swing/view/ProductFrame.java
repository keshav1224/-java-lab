package com.shopsphere.swing.view;

import com.shopsphere.swing.db.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProductFrame extends JFrame {
    private final DefaultTableModel model;
    private final JTable table;

    public ProductFrame() {
        setTitle("Products");
        setSize(800, 480);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"ID","Name","Brand","Category","Price","Discount","Stock","Active"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);

        JPanel buttons = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        buttons.add(refresh); buttons.add(add); buttons.add(edit); buttons.add(del);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        refresh.addActionListener(e -> load());
        add.addActionListener(e -> editProduct(null));
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Select a product first"); return; }
            editProduct((Integer) model.getValueAt(r, 0));
        });
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) return;
            int id = (Integer) model.getValueAt(r, 0);
            if (JOptionPane.showConfirmDialog(this, "Delete product #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try (Connection c = DB.ds().getConnection();
                 PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE product_id=?")) {
                ps.setInt(1, id); ps.executeUpdate(); load();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        load();
    }

    private void load() {
        model.setRowCount(0);
        try (Connection c = DB.ds().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT p.product_id,p.name,p.brand,c.name AS cat,p.price,p.discount,p.stock,p.status " +
                     "FROM products p JOIN categories c ON p.category_id=c.category_id ORDER BY p.product_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDouble(5), rs.getDouble(6), rs.getInt(7), rs.getBoolean(8)
                });
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void editProduct(Integer id) {
        JTextField name = new JTextField();
        JTextField brand = new JTextField();
        JTextField category = new JTextField();
        JTextField price = new JTextField();
        JTextField discount = new JTextField();
        JTextField stock = new JTextField();
        JCheckBox active = new JCheckBox("Active", true);

        if (id != null) {
            try (Connection c = DB.ds().getConnection();
                 PreparedStatement ps = c.prepareStatement("SELECT name,brand,category_id,price,discount,stock,status FROM products WHERE product_id=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        name.setText(rs.getString(1)); brand.setText(rs.getString(2));
                        category.setText(String.valueOf(rs.getInt(3)));
                        price.setText(rs.getString(4)); discount.setText(rs.getString(5));
                        stock.setText(rs.getString(6)); active.setSelected(rs.getBoolean(7));
                    }
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); return; }
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Name:")); panel.add(name);
        panel.add(new JLabel("Brand:")); panel.add(brand);
        panel.add(new JLabel("Category ID:")); panel.add(category);
        panel.add(new JLabel("Price:")); panel.add(price);
        panel.add(new JLabel("Discount %:")); panel.add(discount);
        panel.add(new JLabel("Stock:")); panel.add(stock);
        panel.add(new JLabel("")); panel.add(active);

        int res = JOptionPane.showConfirmDialog(this, panel, id == null ? "Add Product" : "Edit Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        try (Connection c = DB.ds().getConnection()) {
            if (id == null) {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO products(category_id,name,brand,description,price,discount,stock,image_url,status) VALUES(?,?,?,?,?,?,?,?,?)")) {
                    ps.setInt(1, Integer.parseInt(category.getText()));
                    ps.setString(2, name.getText());
                    ps.setString(3, brand.getText());
                    ps.setString(4, "");
                    ps.setDouble(5, Double.parseDouble(price.getText()));
                    ps.setDouble(6, discount.getText().isEmpty() ? 0 : Double.parseDouble(discount.getText()));
                    ps.setInt(7, Integer.parseInt(stock.getText()));
                    ps.setString(8, "");
                    ps.setBoolean(9, active.isSelected());
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE products SET category_id=?,name=?,brand=?,price=?,discount=?,stock=?,status=? WHERE product_id=?")) {
                    ps.setInt(1, Integer.parseInt(category.getText()));
                    ps.setString(2, name.getText());
                    ps.setString(3, brand.getText());
                    ps.setDouble(4, Double.parseDouble(price.getText()));
                    ps.setDouble(5, discount.getText().isEmpty() ? 0 : Double.parseDouble(discount.getText()));
                    ps.setInt(6, Integer.parseInt(stock.getText()));
                    ps.setBoolean(7, active.isSelected());
                    ps.setInt(8, id);
                    ps.executeUpdate();
                }
            }
            load();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }
}
