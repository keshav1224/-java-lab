package com.shopsphere.rmi.remote.dto;

import java.io.Serializable;

public class RemoteProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    public int productId;
    public String name;
    public String brand;
    public double price;
    public int stock;

    @Override
    public String toString() {
        return name + " (" + brand + ") — stock " + stock + " — ₹" + price;
    }
}
