<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<div class="col">
    <div class="card h-100 shadow-sm product-card">
        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-decoration-none">
            <img src="${product.imageUrl}" class="card-img-top product-img" alt="${product.name}">
        </a>
        <div class="card-body d-flex flex-column">
            <span class="badge bg-secondary align-self-start mb-1">${product.brand}</span>
            <h6 class="card-title">
                <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-dark text-decoration-none">
                    ${product.name}
                </a>
            </h6>
            <div class="mt-auto">
                <c:choose>
                    <c:when test="${product.discount > 0}">
                        <span class="text-danger fw-bold">₹<fmt:formatNumber value="${product.discountedPrice}" maxFractionDigits="0"/></span>
                        <small class="text-muted text-decoration-line-through ms-1">₹<fmt:formatNumber value="${product.price}" maxFractionDigits="0"/></small>
                        <span class="badge bg-success ms-1">${product.discount}% off</span>
                    </c:when>
                    <c:otherwise>
                        <span class="fw-bold">₹<fmt:formatNumber value="${product.price}" maxFractionDigits="0"/></span>
                    </c:otherwise>
                </c:choose>
                <div class="mt-2">
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="d-inline">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <button class="btn btn-sm btn-primary"><i class="bi bi-cart-plus"></i> Add</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-sm btn-secondary" disabled>Out of stock</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
