package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public void addOrIncrement(int userId, int productId, int qty) throws SQLException {
        String sql = "INSERT INTO cart(user_id,product_id,quantity) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            ps.executeUpdate();
        }
    }

    public void setQuantity(int userId, int productId, int qty) throws SQLException {
        if (qty <= 0) {
            remove(userId, productId);
            return;
        }
        String sql = "UPDATE cart SET quantity=? WHERE user_id=? AND product_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    public void remove(int userId, int productId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cart WHERE user_id=? AND product_id=?")) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void clear(int userId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cart WHERE user_id=?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public List<CartItem> findByUser(int userId) throws SQLException {
        String sql = "SELECT ct.cart_id, ct.product_id, ct.quantity, " +
                "p.name AS product_name, p.image_url, p.price, p.discount, p.stock " +
                "FROM cart ct JOIN products p ON ct.product_id=p.product_id WHERE ct.user_id=?";
        List<CartItem> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem it = new CartItem();
                    it.setCartId(rs.getInt("cart_id"));
                    it.setProductId(rs.getInt("product_id"));
                    it.setQuantity(rs.getInt("quantity"));
                    it.setProductName(rs.getString("product_name"));
                    it.setImageUrl(rs.getString("image_url"));
                    it.setPrice(rs.getDouble("price"));
                    it.setDiscount(rs.getDouble("discount"));
                    it.setAvailableStock(rs.getInt("stock"));
                    list.add(it);
                }
            }
        }
        return list;
    }

    public int countItems(int userId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COALESCE(SUM(quantity),0) FROM cart WHERE user_id=?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
