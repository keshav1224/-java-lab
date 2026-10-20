<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin - Products"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3 class="mb-3">Manage Products</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <h6 class="mb-0">All Products (${products.size()})</h6>
                        <a class="btn btn-sm btn-success" href="${pageContext.request.contextPath}/admin/products?action=new"><i class="bi bi-plus-lg"></i> New Product</a>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-sm align-middle">
                            <thead><tr><th>ID</th><th>Name</th><th>Brand</th><th>Category</th><th class="text-end">Price</th><th class="text-end">Stock</th><th>Status</th><th></th></tr></thead>
                            <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td>${p.productId}</td>
                                    <td>${p.name}</td>
                                    <td>${p.brand}</td>
                                    <td>${p.categoryName}</td>
                                    <td class="text-end">₹<fmt:formatNumber value="${p.price}" maxFractionDigits="0"/></td>
                                    <td class="text-end">${p.stock}</td>
                                    <td><span class="badge bg-${p.status ? 'success' : 'secondary'}">${p.status ? 'Active' : 'Inactive'}</span></td>
                                    <td class="text-end text-nowrap">
                                        <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.productId}">Edit</a>
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline" onsubmit="return confirm('Delete this product?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${p.productId}">
                                            <button class="btn btn-sm btn-outline-danger">Del</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
