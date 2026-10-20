<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Admin - Category Form"/>
<jsp:include page="../fragments/header.jsp"/>
<jsp:include page="../fragments/navbar.jsp"/>

<div class="container my-4">
    <h3>${empty category ? 'New Category' : 'Edit Category'}</h3>
    <div class="row">
        <jsp:include page="_sidebar.jsp"/>
        <div class="col-md-9">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                        <c:if test="${not empty category}"><input type="hidden" name="categoryId" value="${category.categoryId}"></c:if>
                        <div class="mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" name="name" class="form-control" value="${category.name}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <textarea name="description" class="form-control" rows="3">${category.description}</textarea>
                        </div>
                        <div class="form-check form-switch mb-3">
                            <input class="form-check-input" type="checkbox" name="status" id="activeCat" ${empty category or category.status ? 'checked' : ''}>
                            <label class="form-check-label" for="activeCat">Active</label>
                        </div>
                        <button class="btn btn-primary">Save</button>
                        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/admin/categories">Cancel</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../fragments/footer.jsp"/>
