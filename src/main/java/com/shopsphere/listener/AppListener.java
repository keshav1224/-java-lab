package com.shopsphere.listener;

import com.shopsphere.util.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class AppListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DBConnection.getDataSource().getConnection().close();
            event.getServletContext().setAttribute("appName", "ShopSphere");
            System.out.println("[ShopSphere] Application started — DB pool ready.");
        } catch (Exception e) {
            System.err.println("[ShopSphere] DB pool init FAILED: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        DBConnection.shutdown();
        System.out.println("[ShopSphere] Application shutdown — pool closed.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("[ShopSphere] Session created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("[ShopSphere] Session destroyed: " + se.getSession().getId());
    }
}
