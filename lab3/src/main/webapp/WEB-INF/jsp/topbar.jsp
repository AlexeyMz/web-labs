<%@ page import="java.util.ResourceBundle" %>
<%@ page import="ru.alexeymz.web.controllers.BaseAppController" %>
<%@ page import="ru.alexeymz.web.model.CartEntry" %>
<%@ page import="java.util.List" %>
<% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
<div class="topbar">
    <div class="topbar-wrapper">
        <div class="store-name"><a href="item-list">
            <%= l10n.getString("store.title") %></a>
        </div>
        <div>
            <div class="button"><%= l10n.getString("topbar.signin") %></div>
            <div class="button"><a href="cart">
                <%= l10n.getString("topbar.cart") %></a>
                <% List<CartEntry> entries = (List<CartEntry>)request.getSession().getAttribute("entries");%>
                <% if (entries != null && !entries.isEmpty()) { %>
                    <span>(<%= entries.size() %>)</span>
                <% } %>
            </div>
            <div class="button"><%= l10n.getString("topbar.history") %></div>
            <form class="language-switch">
                <label for="lang"><%= l10n.getString("store.language_switch.label") %>: </label>
                <select id="lang" name="lang" onchange="submit()">
                <% for (BaseAppController.Language lang :
                          (BaseAppController.Language[]) request.getAttribute("languages")) { %>
                    <% boolean selected = lang.code.equals(request.getAttribute("langCode")); %>
                    <option value="<%= lang.code %>" <%= selected ? "selected" : "" %>><%= lang.label %></option>
                <% } %>
                </select>
            </form>
        </div>
    </div>
</div>
