package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO dao = new ProductDAO();

    public List<Product> search(String keyword, Integer categoryId, String sort) throws SQLException {
        return dao.search(keyword, categoryId, sort);
    }

    public Product get(int id) throws SQLException { return dao.findById(id); }
    public List<Product> all() throws SQLException { return dao.findAll(); }
    public List<Product> lowStock(int threshold) throws SQLException { return dao.findLowStock(threshold); }
    public int create(Product p) throws SQLException { return dao.insert(p); }
    public void update(Product p) throws SQLException { dao.update(p); }
    public void delete(int id) throws SQLException { dao.delete(id); }
}
