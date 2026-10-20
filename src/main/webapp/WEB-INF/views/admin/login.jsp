<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin Login"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-5" style="max-width: 440px;">
    <div class="card shadow border-0">
        <div class="card-body p-4">
            <h4 class="text-center mb-3"><i class="bi bi-shield-lock"></i> Admin Login</h4>
            <jsp:include page="../fragments/alerts.jsp"/>
            <form method="post" action="${pageContext.request.contextPath}/admin/login">
                <div class="mb-3"><label class="form-label">Email</label><input type="email" name="email" class="form-control" required></div>
                <div class="mb-3"><label class="form-label">Password</label><input type="password" name="password" class="form-control" required></div>
                <button class="btn btn-dark w-100">Sign In</button>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
