package com.shopsphere.service;

import com.shopsphere.dao.*;
import com.shopsphere.model.*;

import java.sql.SQLException;
import java.util.List;

public class CheckoutService {
    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO itemDAO = new OrderItemDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CouponDAO couponDAO = new CouponDAO();
    private final StripeService stripe = new StripeService();

    public Order placeOrder(int userId, int addressId, String couponCode, String paymentMethod) throws Exception {
        List<CartItem> items = cartDAO.findByUser(userId);
        if (items.isEmpty()) throw new IllegalStateException("Cart is empty");

        // Validate stock
        for (CartItem it : items) {
            Product p = productDAO.findById(it.getProductId());
            if (p == null || !p.isStatus()) throw new IllegalStateException("Product unavailable: " + it.getProductName());
            if (p.getStock() < it.getQuantity()) throw new IllegalStateException("Out of stock: " + p.getName());
        }

        double subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        double discount = 0;
        Coupon coupon = null;
        if (couponCode != null && !couponCode.isBlank()) {
            coupon = couponDAO.findByCode(couponCode.trim().toUpperCase());
            if (coupon != null && subtotal >= coupon.getMinimumOrder()) {
                if ("PERCENT".equalsIgnoreCase(coupon.getDiscountType())) {
                    discount = subtotal * coupon.getDiscountValue() / 100.0;
                } else {
                    discount = coupon.getDiscountValue();
                }
                if (coupon.getMaximumDiscount() != null && discount > coupon.getMaximumDiscount()) {
                    discount = coupon.getMaximumDiscount();
                }
                discount = Math.min(discount, subtotal);
            }
        }
        double total = subtotal - discount;

        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setTotalAmount(total);
        order.setDiscount(discount);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PLACED");
        int orderId = orderDAO.insert(order);
        order.setOrderId(orderId);

        for (CartItem it : items) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(orderId);
            oi.setProductId(it.getProductId());
            oi.setQuantity(it.getQuantity());
            oi.setPrice(it.getPrice());
            itemDAO.insert(oi);
            productDAO.updateStock(it.getProductId(), it.getQuantity());
        }

        // Payment
        String txnRef = stripe.createPaymentIntent(total, "order-" + orderId);
        Payment pay = new Payment();
        pay.setOrderId(orderId);
        pay.setPaymentMethod(paymentMethod);
        pay.setTransactionReference(txnRef);
        pay.setAmount(total);
        pay.setPaymentStatus("SUCCESS");
        paymentDAO.insert(pay);

        orderDAO.updateStatus(orderId, "CONFIRMED");
        order.setOrderStatus("CONFIRMED");
        order.setPaymentStatus("PAID");

        // Convert PENDING -> PAID via payment_status column
        updateOrderPaymentStatus(orderId);

        if (coupon != null && discount > 0) {
            couponDAO.recordUsage(coupon.getCouponId(), userId, orderId);
        }
        cartDAO.clear(userId);
        return order;
    }

    private void updateOrderPaymentStatus(int orderId) throws SQLException {
        try (var c = com.shopsphere.util.DBConnection.getDataSource().getConnection();
             var ps = c.prepareStatement("UPDATE orders SET payment_status='PAID' WHERE order_id=?")) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }
}
