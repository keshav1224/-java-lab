<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Profile"/>
<jsp:include page="fragments/header.jsp"/>
<jsp:include page="fragments/navbar.jsp"/>

<div class="container my-4">
    <div class="row">
        <div class="col-md-5">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h5>Account</h5>
                    <p class="mb-1"><strong>Name:</strong> ${sessionScope.user.name}</p>
                    <p class="mb-1"><strong>Email:</strong> ${sessionScope.user.email}</p>
                    <p class="mb-1"><strong>Mobile:</strong> ${sessionScope.user.mobile}</p>
                    <p class="mb-0"><strong>Role:</strong> ${sessionScope.user.role}</p>
                </div>
            </div>
        </div>
        <div class="col-md-7">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h5>Saved Addresses</h5>
                    <c:forEach var="a" items="${addresses}">
                        <div class="border rounded p-2 mb-2">
                            <span class="badge bg-secondary">${a.addressType}</span> ${a.addressLine}, ${a.city}, ${a.state} - ${a.pincode}
                        </div>
                    </c:forEach>
                    <c:if test="${empty addresses}"><p class="text-muted small">No addresses yet.</p></c:if>
                    <hr>
                    <h6>Add Address</h6>
                    <form method="post" action="${pageContext.request.contextPath}/profile" class="row g-2">
                        <div class="col-12"><input name="addressLine" class="form-control" placeholder="Address line" required></div>
                        <div class="col-md-6"><input name="city" class="form-control" placeholder="City" required></div>
                        <div class="col-md-6"><input name="state" class="form-control" placeholder="State" required></div>
                        <div class="col-md-4"><input name="pincode" class="form-control" placeholder="Pincode" pattern="[0-9]{6}" required></div>
                        <div class="col-md-4"><select name="addressType" class="form-select"><option>HOME</option><option>WORK</option><option>OTHER</option></select></div>
                        <div class="col-md-4"><button class="btn btn-primary w-100">Add</button></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
