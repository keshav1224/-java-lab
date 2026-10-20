<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin - Orders"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>Orders</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <table class="table table-sm align-middle">
                        <thead><tr><th>#</th><th>User</th><th>Address</th><th>Status</th><th>Payment</th><th class="text-end">Total</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td>${o.orderId}</td>
                                <td>User #${o.userId}</td>
                                <td class="small">${o.addressSummary}</td>
                                <td><span class="badge bg-secondary">${o.orderStatus}</span></td>
                                <td><span class="badge bg-${o.paymentStatus eq 'PAID' ? 'success' : 'warning text-dark'}">${o.paymentStatus}</span></td>
                                <td class="text-end">₹<fmt:formatNumber value="${o.totalAmount}" maxFractionDigits="0"/></td>
                                <td><a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/orders?action=view&id=${o.orderId}">View</a></td>
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
