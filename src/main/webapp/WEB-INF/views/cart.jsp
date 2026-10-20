<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Cart"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <h3 class="mb-3"><i class="bi bi-cart3"></i> Shopping Cart</h3>
    <c:if test="${empty items}">
        <div class="alert alert-info">Your cart is empty. <a href="${pageContext.request.contextPath}/products">Continue shopping</a>.</div>
    </c:if>
    <c:if test="${not empty items}">
        <div class="row">
            <div class="col-md-8">
                <c:forEach var="it" items="${items}">
                    <div class="card mb-3 shadow-sm border-0">
                        <div class="card-body d-flex align-items-center">
                            <img src="${it.imageUrl}" style="width:90px;height:90px;object-fit:cover;border-radius:8px;" alt="">
                            <div class="ms-3 flex-grow-1">
                                <h6 class="mb-1">${it.productName}</h6>
                                <div class="text-muted small">Unit: ₹<fmt:formatNumber value="${it.unitPrice}" maxFractionDigits="0"/></div>
                            </div>
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex align-items-center gap-2">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="productId" value="${it.productId}">
                                <input type="number" name="qty" value="${it.quantity}" min="1" max="${it.availableStock}" class="form-control form-control-sm" style="width:80px;">
                                <button class="btn btn-sm btn-outline-secondary">Update</button>
                            </form>
                            <div class="ms-3 fw-bold" style="min-width:90px;text-align:right;">₹<fmt:formatNumber value="${it.subtotal}" maxFractionDigits="0"/></div>
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="ms-2">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="productId" value="${it.productId}">
                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-trash"></i></button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h5>Summary</h5>
                        <div class="d-flex justify-content-between"><span>Subtotal</span><strong>₹<fmt:formatNumber value="${subtotal}" maxFractionDigits="0"/></strong></div>
                        <div class="d-flex justify-content-between text-muted small"><span>Shipping</span><span>Free</span></div>
                        <hr>
                        <a class="btn btn-success w-100" href="${pageContext.request.contextPath}/checkout">
                            <i class="bi bi-credit-card"></i> Checkout
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="fragments/footer.jsp"/>
