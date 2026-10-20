package com.shopsphere.swing.view;

import com.shopsphere.swing.model.AdminUser;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("ShopSphere Admin Dashboard — " + AdminUser.name);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton products = new JButton("Manage Products");
        JButton categories = new JButton("Manage Categories");
        JButton customers = new JButton("View Customers");
        JButton orders = new JButton("Manage Orders");
        JButton inventory = new JButton("Inventory / Low Stock");
        JButton exit = new JButton("Logout & Exit");

        panel.add(products); panel.add(categories); panel.add(customers);
        panel.add(orders); panel.add(inventory); panel.add(exit);

        products.addActionListener(e -> new ProductFrame().setVisible(true));
        categories.addActionListener(e -> new CategoryFrame().setVisible(true));
        customers.addActionListener(e -> new CustomerFrame().setVisible(true));
        orders.addActionListener(e -> new OrderFrame().setVisible(true));
        inventory.addActionListener(e -> new InventoryFrame().setVisible(true));
        exit.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        add(panel);
    }
}
