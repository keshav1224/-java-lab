package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {
    public Coupon findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM coupons WHERE code=? AND status=1 AND (expiry_date IS NULL OR expiry_date >= CURDATE())";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Coupon> findAll() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM coupons ORDER BY coupon_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public int insert(Coupon c0) throws SQLException {
        String sql = "INSERT INTO coupons(code,discount_type,discount_value,minimum_order,maximum_discount,expiry_date,status) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c0.getCode());
            ps.setString(2, c0.getDiscountType());
            ps.setDouble(3, c0.getDiscountValue());
            ps.setDouble(4, c0.getMinimumOrder());
            if (c0.getMaximumDiscount() == null) ps.setNull(5, Types.DECIMAL);
            else ps.setDouble(5, c0.getMaximumDiscount());
            ps.setDate(6, c0.getExpiryDate());
            ps.setBoolean(7, c0.isStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void recordUsage(int couponId, int userId, int orderId) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO coupon_usage(coupon_id,user_id,order_id) VALUES(?,?,?)")) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        }
    }

    private Coupon map(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCode(rs.getString("code"));
        c.setDiscountType(rs.getString("discount_type"));
        c.setDiscountValue(rs.getDouble("discount_value"));
        c.setMinimumOrder(rs.getDouble("minimum_order"));
        double max = rs.getDouble("maximum_discount");
        c.setMaximumDiscount(rs.wasNull() ? null : max);
        c.setExpiryDate(rs.getDate("expiry_date"));
        c.setStatus(rs.getBoolean("status"));
        return c;
    }
}
