package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    public void upsert(Review r) throws SQLException {
        String sql = "INSERT INTO reviews(user_id,product_id,rating,review_text) VALUES(?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE rating=VALUES(rating), review_text=VALUES(review_text)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getProductId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getReviewText());
            ps.executeUpdate();
        }
    }

    public List<Review> findByProduct(int productId) throws SQLException {
        String sql = "SELECT r.*, u.name AS user_name FROM reviews r JOIN users u ON r.user_id=u.user_id " +
                "WHERE r.product_id=? ORDER BY r.review_id DESC";
        List<Review> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setReviewId(rs.getInt("review_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    public double averageRating(int productId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COALESCE(AVG(rating),0) FROM reviews WHERE product_id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        }
    }

    public int countByProduct(int productId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM reviews WHERE product_id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
