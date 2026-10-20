package com.shopsphere.model;

import java.io.Serializable;

public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private int wishlistId;
    private int productId;
    private String productName;
    private String imageUrl;
    private double price;
    private double discount;

    public double getDiscountedPrice() { return price - (price * discount / 100.0); }

    public int getWishlistId() { return wishlistId; }
    public void setWishlistId(int wishlistId) { this.wishlistId = wishlistId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}
