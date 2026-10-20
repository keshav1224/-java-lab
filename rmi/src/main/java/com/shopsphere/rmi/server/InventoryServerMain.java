package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class InventoryServerMain {
    public static void main(String[] args) throws Exception {
        int port = 1099;
        try {
            LocateRegistry.createRegistry(port);
            System.out.println("[RMI] Registry started on port " + port);
        } catch (Exception e) {
            System.out.println("[RMI] Registry already running on port " + port);
        }
        InventoryService svc = new InventoryServiceImpl();
        Naming.rebind("//localhost:" + port + "/InventoryService", svc);
        System.out.println("[RMI] InventoryService bound. Server ready.");
    }
}
