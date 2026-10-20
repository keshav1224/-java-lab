<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin - Product Form"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>${empty product ? 'New Product' : 'Edit Product'}</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/products" method="post">
                        <c:if test="${not empty product}"><input type="hidden" name="productId" value="${product.productId}"></c:if>
                        <div class="row g-3">
                            <div class="col-md-8">
                                <label class="form-label">Name</label>
                                <input type="text" name="name" class="form-control" value="${product.name}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Brand</label>
                                <input type="text" name="brand" class="form-control" value="${product.brand}">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Category</label>
                                <select name="categoryId" class="form-select" required>
                                    <c:forEach var="c" items="${categories}">
                                        <option value="${c.categoryId}" ${product.categoryId == c.categoryId ? 'selected' : ''}>${c.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Image URL</label>
                                <input type="text" name="imageUrl" class="form-control" value="${product.imageUrl}">
                            </div>
                            <div class="col-12">
                                <label class="form-label">Description</label>
                                <textarea name="description" class="form-control" rows="3">${product.description}</textarea>
                            </div>
                            <div class="col-md-3"><label class="form-label">Price (₹)</label><input type="number" step="0.01" name="price" class="form-control" value="${product.price}" required></div>
                            <div class="col-md-3"><label class="form-label">Discount %</label><input type="number" step="0.01" name="discount" class="form-control" value="${product.discount}"></div>
                            <div class="col-md-3"><label class="form-label">Stock</label><input type="number" name="stock" class="form-control" value="${product.stock}" required></div>
                            <div class="col-md-3">
                                <label class="form-label d-block">Status</label>
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" name="status" id="activeSwitch" ${empty product or product.status ? 'checked' : ''}>
                                    <label class="form-check-label" for="activeSwitch">Active</label>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <button class="btn btn-primary">Save</button>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/products">Cancel</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
