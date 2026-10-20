package com.shopsphere.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;

public final class DBConnection {
    private static HikariDataSource dataSource;

    private DBConnection() {}

    public static synchronized HikariDataSource getDataSource() {
        if (dataSource == null) {
            try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties p = new Properties();
                if (in == null) throw new IllegalStateException("db.properties not found");
                p.load(in);
                HikariConfig cfg = new HikariConfig();
                cfg.setJdbcUrl(p.getProperty("db.url"));
                cfg.setUsername(p.getProperty("db.username"));
                cfg.setPassword(p.getProperty("db.password"));
                cfg.setDriverClassName(p.getProperty("db.driver"));
                cfg.setMaximumPoolSize(10);
                cfg.setPoolName("ShopSpherePool");
                dataSource = new HikariDataSource(cfg);
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return dataSource;
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) dataSource.close();
    }
}
