<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-5" style="max-width: 520px;">
    <div class="card shadow-sm border-0">
        <div class="card-body p-4">
            <h3 class="mb-3 text-center">Create Account</h3>
            <jsp:include page="fragments/alerts.jsp"/>
            <form method="post" action="${pageContext.request.contextPath}/register">
                <div class="mb-3">
                    <label class="form-label">Full name</label>
                    <input type="text" name="name" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Mobile</label>
                    <input type="text" name="mobile" class="form-control" pattern="[0-9]{10}">
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" minlength="6" required>
                </div>
                <button class="btn btn-success w-100">Register</button>
            </form>
            <hr>
            <p class="text-center small mb-0">Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
