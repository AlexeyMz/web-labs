<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="cart.title" /> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value='cart.title'/>
    </jsp:include>
    <div class="total-info">
        <span>
            <fmt:message key="cart.total" />:
            <span class="price"><fmt:formatNumber value="${sessionScope.total}"
                                     minFractionDigits="2" type="currency"
                                     currencyCode="USD" /></span>
            <c:choose>
                <c:when test="${pageContext.request.remoteUser != null}">
                    <button><fmt:message key="cart.proceed_order" /></button>
                </c:when>
                <c:otherwise>
                    <button><fmt:message key="cart.sign_in_to_proceed" /></button>
                </c:otherwise>
            </c:choose>
        </span>
    </div>
    <ul>
        <c:forEach var="entry" items="${sessionScope.entries}">
            <li class="cart-entry">
                <div>
                    <a href="item?id=${entry.card.id}">
                    <c:choose>
                        <c:when test="${fn:length(entry.card.imageIds) == 0}">
                            <img height="100" align="left" src="card_images/no_image.jpg" />
                        </c:when>
                        <c:otherwise>
                            <img height="100" align="left" src="card_images/${entry.card.imageIds[0]}.jpg" />
                        </c:otherwise>
                    </c:choose>
                    </a>
                    <div class="quantity-controls">
                        <span><fmt:message key="cart.quantity" />: </span>
                        <button onclick="post('cart', {add: ${entry.card.id}})">+</button>
                        <c:out value="${entry.quantity}" />
                        <button onclick="post('cart', {remove: ${entry.card.id}})">-</button>
                    </div>
                    <div class="price-info">
                        <fmt:message key="cart.price" />:
                        <fmt:formatNumber value="${entry.card.price}"
                                          minFractionDigits="2"
                                          type="currency" currencyCode="USD" />
                        x
                        <fmt:formatNumber value="${entry.quantity}" />
                        =
                        <fmt:formatNumber value="${entry.card.price * entry.quantity}"
                                          minFractionDigits="2"
                                          type="currency" currencyCode="USD" />
                    </div>
                    <h4><c:out value="${entry.card.name}" /></h4>
                    <div clear="both" style="clear: both;"></div>
                </div>
            </li>
        </c:forEach>
    </ul>
</body>
</html>
