<%@ page import="ru.alexeymz.web.model.CartEntry" %>
<%@ page import="ru.alexeymz.web.config.Language" %>
<%@ page import="ru.alexeymz.web.model.User" %>
<%@ page import="ru.alexeymz.web.core.utils.StreamUtils" %>
<%@ page import="java.util.*" %>
<%@ page import="ru.alexeymz.web.core.utils.EscapeUtils" %>
<% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
<div class="topbar">
    <div class="topbar-wrapper">
        <div class="store-name"><a href="item-list">
            <%= l10n.getString(request.getParameter("title-key")) %> |
            <%= l10n.getString("store.title") %></a>
        </div>
        <div class="profile-bar">
            <div class="button">
                <% User user = (User)request.getAttribute("user"); %>
                <% if (user == null) { %>
                    <a href="profile"><%= l10n.getString("topbar.signin") %></a>
                <% } else { %>
                    <%= l10n.getString("topbar.welcome") %>
                    <a href="profile">
                        <% if (user.getAvatar() != null) { %>
                            <img src="data:image/jpg;base64,<%= user.getAvatar() %>" />
                        <% } %>
                        <span class="username"><%= user.getFirstName() %></span>
                    </a>
                    (<span class="signout"><a href="signout.jsp"><%= l10n.getString("topbar.signout") %></a></span>)
                <% } %>
            </div>
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
                <% for (Language lang : (Language[])request.getServletContext().getAttribute("languages")) { %>
                    <% boolean selected = lang.code.equals(request.getAttribute("langCode")); %>
                    <option value="<%= lang.code %>" <%= selected ? "selected" : "" %>><%= lang.label %></option>
                <% } %>
                </select>
                <% for (Map.Entry<String, String[]> pair : request.getParameterMap().entrySet()) { %>
                    <% if (pair.getKey().equals("title-key") || pair.getKey().equals("lang")) { continue; } %>
                    <input type="hidden" name="<%= pair.getKey() %>"
                           value="<%= EscapeUtils.joinParameterValues(pair.getValue()) %>" />
                <% } %>
            </form>
        </div>
    </div>
</div>
