package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> search(String keyword, Integer categoryId, String sort) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS cat_name FROM products p JOIN categories c ON p.category_id=c.category_id WHERE p.status=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like); params.add(like); params.add(like);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND p.category_id=?");
            params.add(categoryId);
        }

        String order = "p.product_id DESC";
        if ("price_asc".equals(sort))   order = "p.price ASC";
        else if ("price_desc".equals(sort)) order = "p.price DESC";
        else if ("name_asc".equals(sort))   order = "p.name ASC";
        else if ("discount".equals(sort))   order = "p.discount DESC";
        sql.append(" ORDER BY ").append(order);

        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public Product findById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name AS cat_name FROM products p JOIN categories c ON p.category_id=c.category_id WHERE p.product_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Product> findAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS cat_name FROM products p JOIN categories c ON p.category_id=c.category_id ORDER BY p.product_id DESC";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS cat_name FROM products p JOIN categories c ON p.category_id=c.category_id WHERE p.stock <= ? ORDER BY p.stock";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO products(category_id,name,brand,description,price,discount,stock,image_url,status) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id=?,name=?,brand=?,description=?,price=?,discount=?,stock=?,image_url=?,status=? WHERE product_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());
            ps.setInt(10, p.getProductId());
            ps.executeUpdate();
        }
    }

    public void updateStock(int productId, int delta) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE product_id=?";
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, delta);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE product_id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countAll() throws SQLException {
        try (Connection c = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM products");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        try { p.setCategoryName(rs.getString("cat_name")); } catch (SQLException ignored) {}
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }
}
