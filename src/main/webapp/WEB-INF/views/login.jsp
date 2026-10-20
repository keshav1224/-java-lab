<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-5" style="max-width: 460px;">
    <div class="card shadow-sm border-0">
        <div class="card-body p-4">
            <h3 class="mb-3 text-center">Login</h3>
            <jsp:include page="fragments/alerts.jsp"/>
            <form method="post" action="${pageContext.request.contextPath}/login">
                <c:if test="${not empty param.next}">
                    <input type="hidden" name="next" value="${param.next}">
                </c:if>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required autofocus>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" required>
                </div>
                <button class="btn btn-primary w-100">Login</button>
            </form>
            <hr>
            <p class="text-center small mb-0">
                New customer? <a href="${pageContext.request.contextPath}/register">Create an account</a><br>
                Admin? <a href="${pageContext.request.contextPath}/admin/login">Admin login</a>
            </p>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
