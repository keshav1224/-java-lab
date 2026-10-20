package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.model.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartService {
    private final CartDAO dao = new CartDAO();

    public void add(int userId, int productId, int qty) throws SQLException {
        dao.addOrIncrement(userId, productId, qty);
    }
    public void update(int userId, int productId, int qty) throws SQLException {
        dao.setQuantity(userId, productId, qty);
    }
    public void remove(int userId, int productId) throws SQLException {
        dao.remove(userId, productId);
    }
    public List<CartItem> list(int userId) throws SQLException { return dao.findByUser(userId); }
    public int count(int userId) throws SQLException { return dao.countItems(userId); }
    public double subtotal(int userId) throws SQLException {
        return dao.findByUser(userId).stream().mapToDouble(CartItem::getSubtotal).sum();
    }
    public void clear(int userId) throws SQLException { dao.clear(userId); }
}
