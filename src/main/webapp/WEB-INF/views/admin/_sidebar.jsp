<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="col-md-3 mb-3">
    <div class="list-group shadow-sm">
        <a class="list-group-item list-group-item-action ${fn:contains(pageContext.request.requestURI,'dashboard') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-speedometer2"></i> Dashboard
        </a>
        <a class="list-group-item list-group-item-action ${fn:contains(pageContext.request.requestURI,'/admin/products') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/products">
            <i class="bi bi-box-seam"></i> Products
        </a>
        <a class="list-group-item list-group-item-action ${fn:contains(pageContext.request.requestURI,'/admin/categories') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/categories">
            <i class="bi bi-tags"></i> Categories
        </a>
        <a class="list-group-item list-group-item-action ${fn:contains(pageContext.request.requestURI,'/admin/orders') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/orders">
            <i class="bi bi-bag-check"></i> Orders
        </a>
        <a class="list-group-item list-group-item-action ${fn:contains(pageContext.request.requestURI,'/admin/customers') ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/customers">
            <i class="bi bi-people"></i> Customers
        </a>
        <a class="list-group-item list-group-item-action" href="${pageContext.request.contextPath}/">
            <i class="bi bi-shop"></i> View Storefront
        </a>
    </div>
</div>
