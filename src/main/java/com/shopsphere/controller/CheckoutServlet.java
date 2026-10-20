package com.shopsphere.controller;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.CouponService;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final CouponService couponService = new CouponService();
    private final AddressDAO addressDAO = new AddressDAO();
    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            var items = cartService.list(u.getUserId());
            if (items.isEmpty()) { resp.sendRedirect(req.getContextPath() + "/cart"); return; }
            double subtotal = items.stream().mapToDouble(ci -> ci.getSubtotal()).sum();
            req.setAttribute("items", items);
            req.setAttribute("subtotal", subtotal);
            req.setAttribute("addresses", addressDAO.findByUser(u.getUserId()));

            String coupon = req.getParameter("coupon");
            if (coupon != null && !coupon.isBlank()) {
                double discount = couponService.apply(coupon, subtotal);
                req.setAttribute("couponCode", coupon);
                req.setAttribute("couponDiscount", discount);
                req.setAttribute("total", subtotal - discount);
            } else {
                req.setAttribute("total", subtotal);
            }
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            String newAddr = req.getParameter("newAddress");
            int addressId;
            if (newAddr != null && "true".equals(newAddr)) {
                Address a = new Address();
                a.setUserId(u.getUserId());
                a.setAddressLine(req.getParameter("addressLine"));
                a.setCity(req.getParameter("city"));
                a.setState(req.getParameter("state"));
                a.setPincode(req.getParameter("pincode"));
                a.setAddressType(req.getParameter("addressType") == null ? "HOME" : req.getParameter("addressType"));
                addressId = addressDAO.insert(a);
            } else {
                addressId = Integer.parseInt(req.getParameter("addressId"));
            }
            String couponCode = req.getParameter("coupon");
            String paymentMethod = req.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.isBlank()) paymentMethod = "STRIPE";

            var order = checkoutService.placeOrder(u.getUserId(), addressId, couponCode, paymentMethod);
            resp.sendRedirect(req.getContextPath() + "/orders?placed=" + order.getOrderId());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
