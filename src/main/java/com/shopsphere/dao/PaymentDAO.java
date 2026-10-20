package com.shopsphere.dao;

import com.shopsphere.model.Payment;
import com.shopsphere.util.DBConnection;

import java.sql.*;

public class PaymentDAO {
    public int insert(Payment p) throws SQLException {
        String sql = "INSERT INTO payments(order_id,payment_method,transaction_reference,amount,payment_status) VALUES(?,?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getPaymentMethod());
            ps.setString(3, p.getTransactionReference());
            ps.setDouble(4, p.getAmount());
            ps.setString(5, p.getPaymentStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void updateStatusByOrder(int orderId, String status) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE payments SET payment_status=? WHERE order_id=?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}
