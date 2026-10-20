package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;

import java.sql.SQLException;
import java.util.List;

public class CouponService {
    private final CouponDAO dao = new CouponDAO();

    /** Returns discount amount for a given cart subtotal, or 0 if not applicable. */
    public double apply(String code, double subtotal) throws SQLException {
        Coupon c = dao.findByCode(code);
        if (c == null) return 0;
        if (subtotal < c.getMinimumOrder()) return 0;
        double discount;
        if ("PERCENT".equalsIgnoreCase(c.getDiscountType())) {
            discount = subtotal * c.getDiscountValue() / 100.0;
        } else {
            discount = c.getDiscountValue();
        }
        if (c.getMaximumDiscount() != null && discount > c.getMaximumDiscount()) {
            discount = c.getMaximumDiscount();
        }
        return Math.min(discount, subtotal);
    }

    public Coupon find(String code) throws SQLException { return dao.findByCode(code); }
    public List<Coupon> all() throws SQLException { return dao.findAll(); }
}
