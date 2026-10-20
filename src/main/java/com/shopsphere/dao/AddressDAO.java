package com.shopsphere.dao;

import com.shopsphere.model.Address;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {
    public int insert(Address a) throws SQLException {
        String sql = "INSERT INTO addresses(user_id,address_line,city,state,pincode,address_type) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getAddressLine());
            ps.setString(3, a.getCity());
            ps.setString(4, a.getState());
            ps.setString(5, a.getPincode());
            ps.setString(6, a.getAddressType() == null ? "HOME" : a.getAddressType());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public List<Address> findByUser(int userId) throws SQLException {
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE user_id = ?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public Address findById(int id) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM addresses WHERE address_id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private Address map(ResultSet rs) throws SQLException {
        Address a = new Address();
        a.setAddressId(rs.getInt("address_id"));
        a.setUserId(rs.getInt("user_id"));
        a.setAddressLine(rs.getString("address_line"));
        a.setCity(rs.getString("city"));
        a.setState(rs.getString("state"));
        a.setPincode(rs.getString("pincode"));
        a.setAddressType(rs.getString("address_type"));
        return a;
    }
}
