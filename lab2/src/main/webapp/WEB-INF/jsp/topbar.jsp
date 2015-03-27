<%@ page import="java.util.ResourceBundle" %>
<%@ page import="ru.alexeymz.web.controllers.BaseAppController" %>
<% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
<div class="topbar">
    <div class="topbar-wrapper">
        <div class="store-name"><%= l10n.getString("store.title") %></div>
        <div>
            <div class="button"><%= l10n.getString("topbar.signin") %></div>
            <div class="button"><%= l10n.getString("topbar.cart") %></div>
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
