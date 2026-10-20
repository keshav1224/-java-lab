<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container mt-4">
    <div class="p-5 mb-4 bg-gradient rounded-3 text-white" style="background: linear-gradient(135deg,#1e3a8a,#4f46e5);">
        <div class="container-fluid py-3">
            <h1 class="display-5 fw-bold">Latest Electronics, Delivered.</h1>
            <p class="col-md-8 fs-5">Laptops, smartphones, audio gear, wearables, and more — with the best deals of the season.</p>
            <a class="btn btn-warning btn-lg" href="${pageContext.request.contextPath}/products">
                <i class="bi bi-shop"></i> Shop Now
            </a>
        </div>
    </div>

    <h2 class="h4 mb-3">🔥 Top Deals</h2>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-3">
        <c:forEach var="p" items="${featured}">
            <t:productCard product="${p}" xmlns:t="urn:jsptagdir:/WEB-INF/tags"/>
        </c:forEach>
    </div>

    <c:if test="${empty featured}">
        <div class="alert alert-info">No products available yet. Please run the SQL script to seed the database.</div>
    </c:if>
</div>

<jsp:include page="fragments/footer.jsp"/>
