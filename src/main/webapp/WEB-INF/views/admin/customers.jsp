<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin - Customers"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>Customers</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <table class="table table-sm align-middle">
                        <thead><tr><th>ID</th><th>Name</th><th>Email</th><th>Mobile</th><th>Role</th><th>Joined</th></tr></thead>
                        <tbody>
                        <c:forEach var="u" items="${customers}">
                            <tr>
                                <td>${u.userId}</td>
                                <td>${u.name}</td>
                                <td>${u.email}</td>
                                <td>${u.mobile}</td>
                                <td><span class="badge bg-${u.role eq 'ADMIN' ? 'danger' : 'primary'}">${u.role}</span></td>
                                <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
