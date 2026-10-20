<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Order ${order.orderId}"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>Order #${order.orderId}</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <div>
                            <div><strong>Customer ID:</strong> ${order.userId}</div>
                            <div><strong>Address:</strong> ${order.addressSummary}</div>
                            <div><strong>Placed:</strong> <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, HH:mm"/></div>
                            <div><strong>Payment:</strong> ${order.paymentMethod} — ${order.paymentStatus}</div>
                        </div>
                        <div class="text-end">
                            <div class="small text-muted">Total</div>
                            <div class="h4">₹<fmt:formatNumber value="${order.totalAmount}" maxFractionDigits="0"/></div>
                            <div class="small text-success">Discount applied: ₹<fmt:formatNumber value="${order.discount}" maxFractionDigits="0"/></div>
                        </div>
                    </div>
                    <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="row g-2 align-items-end">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <div class="col-md-6">
                            <label class="form-label">Update status</label>
                            <select name="status" class="form-select">
                                <c:forEach var="s" items="PLACED,CONFIRMED,SHIPPED,DELIVERED,CANCELLED">
                                    <option ${order.orderStatus eq s ? 'selected' : ''}>${s}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3"><button class="btn btn-primary w-100">Update</button></div>
                    </form>
                </div>
            </div>
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6>Items</h6>
                    <table class="table table-sm align-middle">
                        <thead><tr><th>Product</th><th>Qty</th><th class="text-end">Price</th><th class="text-end">Subtotal</th></tr></thead>
                        <tbody>
                        <c:forEach var="it" items="${order.items}">
                            <tr>
                                <td>${it.productName}</td>
                                <td>${it.quantity}</td>
                                <td class="text-end">₹<fmt:formatNumber value="${it.price}" maxFractionDigits="0"/></td>
                                <td class="text-end">₹<fmt:formatNumber value="${it.subtotal}" maxFractionDigits="0"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
