package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM categories WHERE status=1 ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Category> findAllIncludingInactive() throws SQLException {
        List<Category> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM categories ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public Category findById(int id) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM categories WHERE category_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public int insert(Category cat) throws SQLException {
        String sql = "INSERT INTO categories(name,description,status) VALUES(?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cat.getName());
            ps.setString(2, cat.getDescription());
            ps.setBoolean(3, cat.isStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void update(Category cat) throws SQLException {
        String sql = "UPDATE categories SET name=?,description=?,status=? WHERE category_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getName());
            ps.setString(2, cat.getDescription());
            ps.setBoolean(3, cat.isStatus());
            ps.setInt(4, cat.getCategoryId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM categories WHERE category_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Category map(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setCategoryId(rs.getInt("category_id"));
        cat.setName(rs.getString("name"));
        cat.setDescription(rs.getString("description"));
        cat.setStatus(rs.getBoolean("status"));
        return cat;
    }
}
