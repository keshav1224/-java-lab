<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Checkout"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>Checkout</h3>
    <jsp:include page="fragments/alerts.jsp"/>
    <form method="post" action="${pageContext.request.contextPath}/checkout">
        <div class="row">
            <div class="col-md-7">
                <div class="card shadow-sm border-0 mb-3">
                    <div class="card-body">
                        <h6>Delivery Address</h6>
                        <c:if test="${not empty addresses}">
                            <c:forEach var="a" items="${addresses}" varStatus="s">
                                <div class="form-check mb-2">
                                    <input type="radio" name="addressId" value="${a.addressId}" class="form-check-input" ${s.first ? 'checked' : ''}>
                                    <label class="form-check-label">
                                        <strong>${a.addressType}</strong> — ${a.addressLine}, ${a.city}, ${a.state} - ${a.pincode}
                                    </label>
                                </div>
                            </c:forEach>
                            <hr>
                            <div class="form-check">
                                <input type="radio" name="newAddress" value="false" checked class="form-check-input" id="useOld">
                                <label class="form-check-label" for="useOld">Use selected address above</label>
                            </div>
                            <div class="form-check">
                                <input type="radio" name="newAddress" value="true" class="form-check-input" id="useNew">
                                <label class="form-check-label" for="useNew">Add new address</label>
                            </div>
                        </c:if>
                        <c:if test="${empty addresses}">
                            <input type="hidden" name="newAddress" value="true">
                            <p class="small text-muted">No saved addresses — please add one.</p>
                        </c:if>
                        <div class="row g-2 mt-2">
                            <div class="col-12"><input name="addressLine" class="form-control" placeholder="Address line"></div>
                            <div class="col-md-6"><input name="city" class="form-control" placeholder="City"></div>
                            <div class="col-md-6"><input name="state" class="form-control" placeholder="State"></div>
                            <div class="col-md-4"><input name="pincode" class="form-control" placeholder="Pincode" pattern="[0-9]{6}"></div>
                            <div class="col-md-4">
                                <select name="addressType" class="form-select"><option>HOME</option><option>WORK</option><option>OTHER</option></select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h6>Payment Method</h6>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" value="STRIPE" id="pmStripe" checked>
                            <label class="form-check-label" for="pmStripe"><i class="bi bi-credit-card"></i> Card (Stripe)</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" value="COD" id="pmCod">
                            <label class="form-check-label" for="pmCod">Cash on Delivery</label>
                        </div>
                        <p class="small text-muted mt-2 mb-0">If Stripe is not configured, the app auto-runs in <strong>MOCK payment mode</strong> for the demo.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-5">
                <div class="card shadow-sm border-0">
                    <div class="card-body">
                        <h6>Order Summary</h6>
                        <c:forEach var="it" items="${items}">
                            <div class="d-flex justify-content-between small mb-1">
                                <span>${it.productName} × ${it.quantity}</span>
                                <span>₹<fmt:formatNumber value="${it.subtotal}" maxFractionDigits="0"/></span>
                            </div>
                        </c:forEach>
                        <hr>
                        <div class="d-flex justify-content-between"><span>Subtotal</span><span>₹<fmt:formatNumber value="${subtotal}" maxFractionDigits="0"/></span></div>
                        <c:if test="${not empty couponDiscount and couponDiscount > 0}">
                            <div class="d-flex justify-content-between text-success"><span>Coupon ${couponCode}</span><span>− ₹<fmt:formatNumber value="${couponDiscount}" maxFractionDigits="0"/></span></div>
                        </c:if>
                        <div class="d-flex justify-content-between fw-bold h5 mt-2"><span>Total</span><span>₹<fmt:formatNumber value="${total}" maxFractionDigits="0"/></span></div>
                        <div class="input-group mt-2">
                            <input type="text" name="coupon" class="form-control" placeholder="Coupon code" value="${couponCode}">
                            <button class="btn btn-outline-secondary" formaction="${pageContext.request.contextPath}/checkout" formmethod="get">Apply</button>
                        </div>
                        <button class="btn btn-success w-100 mt-3"><i class="bi bi-check2-circle"></i> Place Order</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="fragments/footer.jsp"/>
