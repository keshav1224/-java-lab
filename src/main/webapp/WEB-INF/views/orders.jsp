<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="My Orders"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>My Orders</h3>
    <c:if test="${not empty placedId}">
        <div class="alert alert-success"><i class="bi bi-check-circle"></i> Order #${placedId} placed successfully!</div>
    </c:if>
    <c:if test="${empty orders}"><div class="alert alert-info">No orders yet.</div></c:if>
    <c:forEach var="o" items="${orders}">
        <div class="card mb-3 shadow-sm border-0">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <div>
                    <strong>Order #${o.orderId}</strong>
                    <small class="text-muted ms-2"><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy, HH:mm"/></small>
                </div>
                <span class="badge bg-${o.orderStatus eq 'DELIVERED' ? 'success' : o.orderStatus eq 'CANCELLED' ? 'danger' : 'primary'}">${o.orderStatus}</span>
            </div>
            <div class="card-body">
                <p class="small text-muted mb-2">Ship to: ${o.addressSummary}</p>
                <table class="table table-sm align-middle">
                    <c:forEach var="it" items="${o.items}">
                        <tr>
                            <td style="width:70px;"><img src="${it.imageUrl}" style="width:60px;height:60px;object-fit:cover;border-radius:6px;"></td>
                            <td>${it.productName}<br><small class="text-muted">₹<fmt:formatNumber value="${it.price}" maxFractionDigits="0"/> × ${it.quantity}</small></td>
                            <td class="text-end">₹<fmt:formatNumber value="${it.subtotal}" maxFractionDigits="0"/></td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="d-flex justify-content-between align-items-center">
                    <span class="small text-muted">Payment: ${o.paymentMethod} — ${o.paymentStatus}</span>
                    <span class="fw-bold">Total: ₹<fmt:formatNumber value="${o.totalAmount}" maxFractionDigits="0"/></span>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<jsp:include page="fragments/footer.jsp"/>
