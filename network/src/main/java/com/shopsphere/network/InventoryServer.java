package com.shopsphere.network;

import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * Socket inventory server. Listens on port 5555.
 * Protocol (line-based text):
 *   GET <id>       -> returns product JSON-like string
 *   STOCK <id>     -> returns integer stock
 *   QUIT           -> close
 */
public class InventoryServer {
    private static final String URL = "jdbc:mysql://localhost:3306/shopsphere?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) throws IOException {
        int port = 5555;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("[Socket] InventoryServer listening on " + port);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }
        }
    }

    private static void handle(Socket s) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("QUIT")) { out.println("BYE"); return; }
                String[] parts = line.split("\\s+");
                if (parts.length < 2) { out.println("ERR usage: GET|STOCK <id>"); continue; }
                try {
                    int id = Integer.parseInt(parts[1]);
                    if (parts[0].equalsIgnoreCase("GET")) out.println(getProduct(id));
                    else if (parts[0].equalsIgnoreCase("STOCK")) out.println(getStock(id));
                    else out.println("ERR unknown command");
                } catch (NumberFormatException e) {
                    out.println("ERR invalid id");
                }
            }
        } catch (Exception e) { System.err.println("[Socket] error: " + e.getMessage()); }
    }

    private static String getProduct(int id) {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement("SELECT name,brand,price,stock FROM products WHERE product_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "PRODUCT id=" + id + " name=\"" + rs.getString(1) + "\" brand=\"" +
                            rs.getString(2) + "\" price=" + rs.getDouble(3) + " stock=" + rs.getInt(4);
                }
            }
        } catch (SQLException e) { return "ERR db " + e.getMessage(); }
        return "NOT_FOUND";
    }

    private static String getStock(int id) {
        try (Connection c = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = c.prepareStatement("SELECT stock FROM products WHERE product_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? "STOCK id=" + id + " value=" + rs.getInt(1) : "NOT_FOUND";
            }
        } catch (SQLException e) { return "ERR db " + e.getMessage(); }
    }
}
