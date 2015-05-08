<%@ page import="ru.alexeymz.web.data.CardRepository" %>
<%@ page import="ru.alexeymz.web.model.Card" %>
<%@ page import="ru.alexeymz.web.model.OrderEntry" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <% CardRepository cardRepository = (CardRepository)request.getAttribute("cardRepo"); %>
    <meta charset="UTF-8">
    <title><fmt:message key="orders.title" /> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value="orders.title"/>
    </jsp:include>
    <c:forEach var="order" items="${requestScope.orders}">
        <div class="order-entry">
            <div><span><fmt:message key="order.purchase_date" />: </span>
                <fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${order.purchaseDate.time}" />
            </div>
            <div><span><fmt:message key="cart.total" />: </span>
                <fmt:formatNumber value="${order.total}"
                              minFractionDigits="2" type="currency"
                              currencyCode="USD" />
            </div>
            <c:if test="${order.deliveryPoint != null}">
                <div><span><fmt:message key="order.delivery_point" />: </span><c:out value="${order.deliveryPoint}" /></div>
            </c:if>
            <c:if test="${order.deliveryAddress != null}">
                <div><span><fmt:message key="order.delivery_address" />: </span><c:out value="${order.deliveryAddress}" /></div>
            </c:if>
            <ul class="card-list">
            <c:forEach var="entry" items="${order.entries}">
                <% OrderEntry entry = (OrderEntry)pageContext.getAttribute("entry"); %>
                <% Card card = cardRepository.findById(entry.getCardId()); %>
                <li class="card-list-item">
                <div>
                    <a href="item?id=<%= card.getId() %>">
                        <% if (card.getImageIds().size() > 0) { %>
                        <img height="100" align="left" src="card_images/<%= card.getImageIds().get(0) %>.jpg" />
                        <% } else { %>
                        <img height="100" align="left" src="card_images/no_image.jpg" />
                        <% } %>
                    </a>
                    <div class="pricing">
                        <fmt:message key="cart.price" />:
                        <span class="price">
                            <fmt:formatNumber value="<%= card.getPrice() %>"
                                              minFractionDigits="2" type="currency"
                                              currencyCode="USD" />
                        </span>
                    </div>
                    <h4><%= card.getName() %></h4>
                    <div><span><fmt:message key="cart.quantity" />: </span><c:out value="${entry.quantity}" /></div>
                    <div clear="both" style="clear: both;"></div>
                </div>
                </li>
            </c:forEach>
            </ul>
        </div>
    </c:forEach>

</body>
</html>
