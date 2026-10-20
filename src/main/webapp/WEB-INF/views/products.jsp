<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="pageTitle" value="Products"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <div class="row">
        <div class="col-md-3 mb-3">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h6 class="fw-bold">Categories</h6>
                    <div class="list-group list-group-flush">
                        <a class="list-group-item list-group-item-action ${empty selectedCategory ? 'active' : ''}"
                           href="${pageContext.request.contextPath}/products">All</a>
                        <c:forEach var="cat" items="${categories}">
                            <a class="list-group-item list-group-item-action ${selectedCategory == cat.categoryId ? 'active' : ''}"
                               href="${pageContext.request.contextPath}/products?category=${cat.categoryId}">${cat.name}</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap">
                <h4 class="mb-2">Products</h4>
                <form class="d-flex gap-2" method="get" action="${pageContext.request.contextPath}/products">
                    <c:if test="${not empty q}"><input type="hidden" name="q" value="${q}"></c:if>
                    <c:if test="${not empty selectedCategory}"><input type="hidden" name="category" value="${selectedCategory}"></c:if>
                    <select name="sort" class="form-select form-select-sm" onchange="this.form.submit()">
                        <option value="">Sort: Default</option>
                        <option value="price_asc" ${sort eq 'price_asc' ? 'selected' : ''}>Price ↑</option>
                        <option value="price_desc" ${sort eq 'price_desc' ? 'selected' : ''}>Price ↓</option>
                        <option value="name_asc" ${sort eq 'name_asc' ? 'selected' : ''}>Name A–Z</option>
                        <option value="discount" ${sort eq 'discount' ? 'selected' : ''}>Best discount</option>
                    </select>
                </form>
            </div>
            <c:if test="${not empty q}">
                <p class="text-muted">Results for "<strong>${q}</strong>" — ${products.size()} found</p>
            </c:if>
            <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-3">
                <c:forEach var="p" items="${products}">
                    <t:productCard product="${p}"/>
                </c:forEach>
            </div>
            <c:if test="${empty products}">
                <div class="alert alert-warning">No products match your search.</div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
