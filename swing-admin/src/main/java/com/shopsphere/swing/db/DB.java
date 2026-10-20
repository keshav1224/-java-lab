package com.shopsphere.swing.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;

public final class DB {
    private static HikariDataSource ds;
    private DB() {}

    public static synchronized HikariDataSource ds() {
        if (ds == null) {
            try (InputStream in = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties p = new Properties();
                if (in == null) throw new IllegalStateException("db.properties not found");
                p.load(in);
                HikariConfig cfg = new HikariConfig();
                cfg.setJdbcUrl(p.getProperty("db.url"));
                cfg.setUsername(p.getProperty("db.username"));
                cfg.setPassword(p.getProperty("db.password"));
                cfg.setDriverClassName(p.getProperty("db.driver"));
                cfg.setMaximumPoolSize(5);
                ds = new HikariDataSource(cfg);
            } catch (Exception e) { throw new RuntimeException(e); }
        }
        return ds;
    }
}
