package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Review;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private final ReviewDAO dao = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        if (u == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating 1-5 only");
            Review r = new Review();
            r.setUserId(u.getUserId());
            r.setProductId(productId);
            r.setRating(rating);
            r.setReviewText(req.getParameter("reviewText"));
            dao.upsert(r);
            resp.sendRedirect(req.getContextPath() + "/product-details?id=" + productId + "#reviews");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
