<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Wishlist"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <h3><i class="bi bi-heart"></i> Wishlist</h3>
    <c:if test="${empty wishlist}"><div class="alert alert-info">Your wishlist is empty.</div></c:if>
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
        <c:forEach var="w" items="${wishlist}">
            <div class="col">
                <div class="card shadow-sm border-0 h-100">
                    <img src="${w.imageUrl}" class="card-img-top" style="height:180px;object-fit:cover;" alt="">
                    <div class="card-body">
                        <h6>${w.productName}</h6>
                        <p class="mb-2">₹<fmt:formatNumber value="${w.discountedPrice}" maxFractionDigits="0"/></p>
                        <div class="d-flex gap-2">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="flex-grow-1">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${w.productId}">
                                <button class="btn btn-sm btn-primary w-100">Add to Cart</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                <input type="hidden" name="productId" value="${w.productId}">
                                <input type="hidden" name="action" value="remove">
                                <button class="btn btn-sm btn-outline-danger"><i class="bi bi-x-lg"></i></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
