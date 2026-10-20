package com.shopsphere.rmi.client;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.Naming;

public class InventoryClientMain {
    public static void main(String[] args) throws Exception {
        InventoryService svc = (InventoryService) Naming.lookup("//localhost:1099/InventoryService");
        System.out.println("[RMI] Connected to remote InventoryService");

        var p = svc.getProduct(1);
        System.out.println("Product from RMI: " + p);

        System.out.println("Stock of product 3: " + svc.getStock(3));
        System.out.println("Update stock of product 1 by -1: " + svc.updateStock(1, -1));
        System.out.println("New stock of product 1: " + svc.getStock(1));
    }
}
