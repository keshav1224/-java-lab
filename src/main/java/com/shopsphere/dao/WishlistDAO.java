package com.shopsphere.dao;

import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public void add(int userId, int productId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT IGNORE INTO wishlist(user_id,product_id) VALUES(?,?)")) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void remove(int userId, int productId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM wishlist WHERE user_id=? AND product_id=?")) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public List<WishlistItem> findByUser(int userId) throws SQLException {
        String sql = "SELECT w.wishlist_id, w.product_id, p.name AS product_name, p.image_url, p.price, p.discount " +
                "FROM wishlist w JOIN products p ON w.product_id=p.product_id WHERE w.user_id=?";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem it = new WishlistItem();
                    it.setWishlistId(rs.getInt("wishlist_id"));
                    it.setProductId(rs.getInt("product_id"));
                    it.setProductName(rs.getString("product_name"));
                    it.setImageUrl(rs.getString("image_url"));
                    it.setPrice(rs.getDouble("price"));
                    it.setDiscount(rs.getDouble("discount"));
                    list.add(it);
                }
            }
        }
        return list;
    }
}
