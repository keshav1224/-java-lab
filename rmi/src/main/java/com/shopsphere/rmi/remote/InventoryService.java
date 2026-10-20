package com.shopsphere.rmi.remote;

import com.shopsphere.rmi.remote.dto.RemoteProduct;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI remote inventory service — RTU syllabus demonstration of RMI architecture,
 * RMI Registry, and serialization of Product objects.
 */
public interface InventoryService extends Remote {
    RemoteProduct getProduct(int id) throws RemoteException;
    int getStock(int productId) throws RemoteException;
    boolean updateStock(int productId, int delta) throws RemoteException;
}
