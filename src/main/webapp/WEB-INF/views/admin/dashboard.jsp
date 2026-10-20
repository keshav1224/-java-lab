<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin Dashboard"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3 class="mb-3">Admin Dashboard</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>

        <div class="col-md-9">
            <div class="row g-3 mb-4">
                <div class="col-6 col-md-3">
                    <div class="card border-0 shadow-sm bg-primary text-white"><div class="card-body">
                        <div class="small">Products</div><div class="h3 mb-0">${productCount}</div></div></div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card border-0 shadow-sm bg-success text-white"><div class="card-body">
                        <div class="small">Orders</div><div class="h3 mb-0">${orderCount}</div></div></div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card border-0 shadow-sm bg-warning text-dark"><div class="card-body">
                        <div class="small">Customers</div><div class="h3 mb-0">${customerCount}</div></div></div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card border-0 shadow-sm bg-dark text-white"><div class="card-body">
                        <div class="small">Revenue</div><div class="h5 mb-0">₹<fmt:formatNumber value="${revenue}" maxFractionDigits="0"/></div></div></div>
                </div>
            </div>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body">
                    <h6 class="fw-bold text-danger"><i class="bi bi-exclamation-triangle"></i> Low stock (≤ 15)</h6>
                    <table class="table table-sm align-middle mb-0">
                        <thead><tr><th>Product</th><th>Brand</th><th class="text-end">Stock</th></tr></thead>
                        <tbody>
                        <c:forEach var="p" items="${lowStock}">
                            <tr>
                                <td>${p.name}</td>
                                <td>${p.brand}</td>
                                <td class="text-end"><span class="badge bg-${p.stock == 0 ? 'danger' : p.stock <= 5 ? 'warning text-dark' : 'secondary'}">${p.stock}</span></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty lowStock}"><tr><td colspan="3" class="text-muted">All products well stocked.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6 class="fw-bold">Recent Orders</h6>
                    <table class="table table-sm align-middle mb-0">
                        <thead><tr><th>#</th><th>Customer</th><th>Status</th><th class="text-end">Total</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="o" items="${recentOrders}">
                            <tr>
                                <td>${o.orderId}</td>
                                <td>User #${o.userId}</td>
                                <td><span class="badge bg-secondary">${o.orderStatus}</span></td>
                                <td class="text-end">₹<fmt:formatNumber value="${o.totalAmount}" maxFractionDigits="0"/></td>
                                <td class="text-end"><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/orders?action=view&id=${o.orderId}">View</a></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty recentOrders}"><tr><td colspan="5" class="text-muted">No orders yet.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
