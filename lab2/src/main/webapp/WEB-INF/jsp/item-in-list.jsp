<%@ page import="ru.alexeymz.web.model.Card" %>
<%@ page import="ru.alexeymz.web.core.utils.EscapeUtils" %>
<%@ page import="ru.alexeymz.web.core.template.Unescaped" %>
<% Card card = (Card) request.getAttribute("card"); %>
<li class="card-list-item">
    <a href="item/<%= card.getId() %>?lang=<%= request.getAttribute("langCode") %>">
        <div>
            <% if (card.getImageIds().size() > 0) { %>
                <img height="200" align="left" src="card_images/<%= card.getImageIds().get(0) %>.jpg" />
            <% } else { %>
                <img height="200" align="left" src="card_images/no_image.jpg" />
            <% } %>
            <div><%= EscapeUtils.escapeWithNewlines(card.getCardText()) %></div>
            <div><%= EscapeUtils.escapeWithNewlines(card.getFlavorText()) %></div>
            <br clear="both" />
        </div>
    </a>
</li>
