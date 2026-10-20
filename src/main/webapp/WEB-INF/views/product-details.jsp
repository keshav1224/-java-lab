<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="pageTitle" value="${product.name}"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products">Products</a></li>
            <li class="breadcrumb-item active">${product.name}</li>
        </ol>
    </nav>

    <div class="row g-4">
        <div class="col-md-6">
            <img src="${product.imageUrl}" class="img-fluid rounded shadow-sm w-100" alt="${product.name}" style="max-height:460px;object-fit:cover;">
        </div>
        <div class="col-md-6">
            <span class="badge bg-secondary">${product.brand}</span>
            <h2 class="mt-2">${product.name}</h2>
            <div class="mb-2">
                <t:rating value="${avgRating}"/>
                <span class="text-muted ms-1">(${reviewCount} reviews)</span>
            </div>
            <p class="text-muted">${product.description}</p>
            <div class="mb-3">
                <c:choose>
                    <c:when test="${product.discount > 0}">
                        <span class="h3 text-danger fw-bold">₹<fmt:formatNumber value="${product.discountedPrice}" maxFractionDigits="0"/></span>
                        <span class="text-muted text-decoration-line-through ms-2">₹<fmt:formatNumber value="${product.price}" maxFractionDigits="0"/></span>
                        <span class="badge bg-success ms-2">${product.discount}% off</span>
                    </c:when>
                    <c:otherwise>
                        <span class="h3 fw-bold">₹<fmt:formatNumber value="${product.price}" maxFractionDigits="0"/></span>
                    </c:otherwise>
                </c:choose>
            </div>
            <p>
                <c:choose>
                    <c:when test="${product.stock > 0}"><span class="badge bg-success">In stock (${product.stock})</span></c:when>
                    <c:otherwise><span class="badge bg-danger">Out of stock</span></c:otherwise>
                </c:choose>
            </p>

            <form action="${pageContext.request.contextPath}/cart" method="post" class="row g-2 align-items-end mb-2">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="productId" value="${product.productId}">
                <div class="col-auto">
                    <label class="form-label small">Qty</label>
                    <input type="number" name="qty" value="1" min="1" max="${product.stock}" class="form-control" style="width:90px;">
                </div>
                <div class="col-auto">
                    <button class="btn btn-primary" ${product.stock == 0 ? 'disabled' : ''}><i class="bi bi-cart-plus"></i> Add to Cart</button>
                </div>
            </form>
            <form action="${pageContext.request.contextPath}/wishlist" method="post">
                <input type="hidden" name="productId" value="${product.productId}">
                <input type="hidden" name="from" value="/product-details?id=${product.productId}">
                <button class="btn btn-outline-danger"><i class="bi bi-heart"></i> Wishlist</button>
            </form>
        </div>
    </div>

    <hr class="my-5">
    <h4 id="reviews">Reviews</h4>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <form action="${pageContext.request.contextPath}/review" method="post" class="mb-4 row g-2 align-items-end">
                <input type="hidden" name="productId" value="${product.productId}">
                <div class="col-md-2">
                    <label class="form-label small">Rating</label>
                    <select name="rating" class="form-select" required>
                        <option value="5">5 ★</option><option value="4">4 ★</option><option value="3">3 ★</option>
                        <option value="2">2 ★</option><option value="1">1 ★</option>
                    </select>
                </div>
                <div class="col-md-8">
                    <label class="form-label small">Your review</label>
                    <input type="text" name="reviewText" class="form-control" placeholder="Share your experience...">
                </div>
                <div class="col-md-2">
                    <button class="btn btn-primary w-100">Submit</button>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <p class="text-muted"><a href="${pageContext.request.contextPath}/login">Login</a> to write a review.</p>
        </c:otherwise>
    </c:choose>

    <c:forEach var="r" items="${reviews}">
        <div class="card mb-2 shadow-sm border-0">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <strong>${r.userName}</strong>
                    <small class="text-muted"><fmt:formatDate value="${r.createdAt}" pattern="dd MMM yyyy"/></small>
                </div>
                <t:rating value="${r.rating}.0"/>
                <p class="mb-0 mt-1">${r.reviewText}</p>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty reviews}"><p class="text-muted">No reviews yet.</p></c:if>
</div>

<jsp:include page="fragments/footer.jsp"/>
