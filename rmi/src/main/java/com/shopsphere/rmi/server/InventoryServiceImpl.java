package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.remote.dto.RemoteProduct;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {
    private static final String URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "root";

    public InventoryServiceImpl() throws RemoteException { super(); }

    @Override
    public RemoteProduct getProduct(int id) throws RemoteException {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement("SELECT * FROM products WHERE product_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RemoteProduct p = new RemoteProduct();
                    p.productId = rs.getInt("product_id");
                    p.name = rs.getString("name");
                    p.brand = rs.getString("brand");
                    p.price = rs.getDouble("price");
                    p.stock = rs.getInt("stock");
                    return p;
                }
            }
        } catch (SQLException e) { throw new RemoteException("DB error", e); }
        return null;
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement("SELECT stock FROM products WHERE product_id=?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException e) { throw new RemoteException("DB error", e); }
    }

    @Override
    public boolean updateStock(int productId, int delta) throws RemoteException {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement("UPDATE products SET stock = stock + ? WHERE product_id=?")) {
            ps.setInt(1, delta);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { throw new RemoteException("DB error", e); }
    }
}
