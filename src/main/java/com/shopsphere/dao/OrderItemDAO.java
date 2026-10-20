package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    public void insert(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items(order_id,product_id,quantity,price) VALUES(?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());
            ps.executeUpdate();
        }
    }

    public List<OrderItem> findByOrder(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.image_url FROM order_items oi " +
                "JOIN products p ON oi.product_id=p.product_id WHERE oi.order_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem it = new OrderItem();
                    it.setOrderItemId(rs.getInt("order_item_id"));
                    it.setOrderId(rs.getInt("order_id"));
                    it.setProductId(rs.getInt("product_id"));
                    it.setProductName(rs.getString("product_name"));
                    it.setImageUrl(rs.getString("image_url"));
                    it.setQuantity(rs.getInt("quantity"));
                    it.setPrice(rs.getDouble("price"));
                    list.add(it);
                }
            }
        }
        return list;
    }
}
