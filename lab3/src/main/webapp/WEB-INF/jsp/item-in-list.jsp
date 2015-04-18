<%@ page import="ru.alexeymz.web.model.Card" %>
<%@ page import="ru.alexeymz.web.core.utils.EscapeUtils" %>
<%@ page import="ru.alexeymz.web.core.template.Unescaped" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
<% Card card = (Card) request.getAttribute("card"); %>
<li class="card-list-item">
    <div>
        <a href="item?id=<%= card.getId() %>">
            <% if (card.getImageIds().size() > 0) { %>
                <img height="200" align="left" src="card_images/<%= card.getImageIds().get(0) %>.jpg" />
            <% } else { %>
                <img height="200" align="left" src="card_images/no_image.jpg" />
            <% } %>
        </a>
        <div class="pricing">
            <fmt:message key="cart.price" />:
            <span class="price">
                <fmt:formatNumber value="${card.price}"
                      minFractionDigits="2" type="currency"
                      currencyCode="USD" />
            </span>
            <button onclick="post('cart', {add: <%= card.getId() %>})">
                <%= l10n.getString("item.add_to_cart.label") %>
            </button>
        </div>
        <h4><%= card.getName() %></h4>
        <div class="cardText"><%= EscapeUtils.escapeWithParagraphs(card.getCardText()) %></div>
        <div class="flavorText"><%= EscapeUtils.escapeWithParagraphs(card.getFlavorText()) %></div>
        <div clear="both" style="clear: both;"></div>
    </div>
</li>
