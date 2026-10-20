<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="value" required="true" type="java.lang.Double" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:forEach begin="1" end="5" var="i">
    <c:choose>
        <c:when test="${i <= value}"><i class="bi bi-star-fill text-warning"></i></c:when>
        <c:when test="${i - 0.5 <= value}"><i class="bi bi-star-half text-warning"></i></c:when>
        <c:otherwise><i class="bi bi-star text-muted"></i></c:otherwise>
    </c:choose>
</c:forEach>
