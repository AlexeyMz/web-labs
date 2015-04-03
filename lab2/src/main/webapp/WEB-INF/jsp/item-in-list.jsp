<%@ page import="ru.alexeymz.web.model.Card" %>
<%@ page import="ru.alexeymz.web.core.utils.EscapeUtils" %>
<%@ page import="ru.alexeymz.web.core.template.Unescaped" %>
<%@ page import="java.util.ResourceBundle" %>
<% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
<% Card card = (Card) request.getAttribute("card"); %>
<li class="card-list-item">
    <div>
        <a href="item/<%= card.getId() %>?lang=<%= request.getAttribute("langCode") %>">
            <% if (card.getImageIds().size() > 0) { %>
                <img height="200" align="left" src="card_images/<%= card.getImageIds().get(0) %>.jpg" />
            <% } else { %>
                <img height="200" align="left" src="card_images/no_image.jpg" />
            <% } %>
        </a>
        <button><%= l10n.getString("item.add_to_cart.label") %></button>
        <h4><%= card.getName() %></h4>
        <div class="cardText"><%= EscapeUtils.escapeWithParagraphs(card.getCardText()) %></div>
        <div class="flavorText"><%= EscapeUtils.escapeWithParagraphs(card.getFlavorText()) %></div>
        <div clear="both" style="clear: both;"></div>
    </div>
</li>
