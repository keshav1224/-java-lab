<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Admin - Categories"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>Manage Categories</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <h6 class="mb-0">Categories</h6>
                        <a class="btn btn-sm btn-success" href="${pageContext.request.contextPath}/admin/categories?action=new"><i class="bi bi-plus-lg"></i> New</a>
                    </div>
                    <table class="table table-sm align-middle">
                        <thead><tr><th>ID</th><th>Name</th><th>Description</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="c" items="${categories}">
                            <tr>
                                <td>${c.categoryId}</td>
                                <td>${c.name}</td>
                                <td>${c.description}</td>
                                <td><span class="badge bg-${c.status ? 'success' : 'secondary'}">${c.status ? 'Active' : 'Inactive'}</span></td>
                                <td class="text-end text-nowrap">
                                    <a class="btn btn-sm btn-outline-primary" href="${pageContext.request.contextPath}/admin/categories?action=edit&id=${c.categoryId}">Edit</a>
                                    <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline" onsubmit="return confirm('Delete?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${c.categoryId}">
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

<jsp:include page="../fragments/footer.jsp"/>
