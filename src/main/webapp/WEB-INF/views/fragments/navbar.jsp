<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">
            <i class="bi bi-lightning-charge-fill text-warning"></i> ShopSphere
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMain">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navMain">
            <form class="d-flex ms-lg-3 my-2 my-lg-0 flex-grow-1" action="${pageContext.request.contextPath}/products" method="get">
                <input class="form-control me-2" name="q" placeholder="Search laptops, phones, audio..." value="${q}">
                <button class="btn btn-outline-light" type="submit"><i class="bi bi-search"></i></button>
            </form>
            <ul class="navbar-nav ms-auto align-items-lg-center">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/products">Products</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart">
                                <i class="bi bi-cart3"></i> Cart
                                <c:if test="${not empty cartCount and cartCount > 0}">
                                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">${cartCount}</span>
                                </c:if>
                            </a>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/wishlist"><i class="bi bi-heart"></i></a></li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#">
                                <i class="bi bi-person-circle"></i> ${sessionScope.user.name}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Profile</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders">My Orders</a></li>
                                <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard"><i class="bi bi-speedometer2"></i> Admin Panel</a></li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li class="nav-item"><a class="btn btn-warning ms-lg-2" href="${pageContext.request.contextPath}/register">Sign Up</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
