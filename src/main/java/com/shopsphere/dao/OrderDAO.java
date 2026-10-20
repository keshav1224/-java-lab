package com.shopsphere.dao;

import com.shopsphere.model.Order;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int insert(Order o) throws SQLException {
        String sql = "INSERT INTO orders(user_id,address_id,total_amount,discount,payment_method,payment_status,order_status) " +
                "VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getUserId());
            ps.setInt(2, o.getAddressId());
            ps.setDouble(3, o.getTotalAmount());
            ps.setDouble(4, o.getDiscount());
            ps.setString(5, o.getPaymentMethod());
            ps.setString(6, o.getPaymentStatus());
            ps.setString(7, o.getOrderStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public List<Order> findByUser(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(a.address_line,', ',a.city,', ',a.state,' - ',a.pincode) AS addr_summary " +
                "FROM orders o JOIN addresses a ON o.address_id=a.address_id " +
                "WHERE o.user_id=? ORDER BY o.order_id DESC";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, CONCAT(a.address_line,', ',a.city) AS addr_summary " +
                "FROM orders o JOIN addresses a ON o.address_id=a.address_id ORDER BY o.order_id DESC";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Order findById(int id) throws SQLException {
        String sql = "SELECT o.*, CONCAT(a.address_line,', ',a.city) AS addr_summary " +
                "FROM orders o JOIN addresses a ON o.address_id=a.address_id WHERE o.order_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE orders SET order_status=? WHERE order_id=?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public double totalRevenue() throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE payment_status='PAID'");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    public int countAll() throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM orders");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setAddressId(rs.getInt("address_id"));
        try { o.setAddressSummary(rs.getString("addr_summary")); } catch (SQLException ignored) {}
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDiscount(rs.getDouble("discount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}
