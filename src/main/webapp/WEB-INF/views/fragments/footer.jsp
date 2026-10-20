<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="bg-dark text-light mt-5 py-4">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <h5><i class="bi bi-lightning-charge-fill text-warning"></i> ShopSphere</h5>
                <p class="small text-muted mb-0">RTU Advanced Java E-Commerce Project — Electronics Store Demo</p>
            </div>
            <div class="col-md-4">
                <h6>Quick Links</h6>
                <ul class="list-unstyled small">
                    <li><a class="text-light text-decoration-none" href="${pageContext.request.contextPath}/products">All Products</a></li>
                    <li><a class="text-light text-decoration-none" href="${pageContext.request.contextPath}/cart">My Cart</a></li>
                    <li><a class="text-light text-decoration-none" href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                </ul>
            </div>
            <div class="col-md-4">
                <h6>Project Info</h6>
                <p class="small text-muted mb-0">Built with Jakarta EE, Servlet, JSP, JDBC, MySQL, Swing, RMI, Socket.</p>
            </div>
        </div>
        <hr class="border-secondary">
        <div class="text-center small text-muted">© 2026 ShopSphere. Educational project.</div>
    </div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
