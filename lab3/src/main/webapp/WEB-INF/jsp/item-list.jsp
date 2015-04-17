<%@ page import="ru.alexeymz.web.model.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <% ResourceBundle l10n = (ResourceBundle) request.getAttribute("l10n"); %>
    <meta charset="UTF-8">
    <title><%= l10n.getString("list.title") %> | <%= l10n.getString("store.title") %></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp" />
    <label for="cardSetFilter"><%= l10n.getString("card.set") %>: </label>
    <select id="cardSetFilter" onchange="onFilterChanged();">
        <option value="none">&mdash;</option>
        <% for (String set : (Collection<String>)request.getAttribute("sets")) { %>
            <% boolean selected = set.equals(request.getAttribute("selectedSet")); %>
            <option value="<%= set %>" <%= selected ? "selected" : "" %>><%= set %></option>
        <% } %>
    </select>
    <ul>
    <% for (Card card : (List<Card>) request.getAttribute("cards")) { %>
        <% request.setAttribute("card", card); %>
        <jsp:include page="item-in-list.jsp" />
    <% } %>
    </ul>
    <script>
        function onFilterChanged() {
            var selectedSet = document.querySelector('#cardSetFilter').value;
            window.location = 'item-list' + (selectedSet == ""
                    ? "" : ('?filter=' + selectedSet));
        }
    </script>
</body>
</html>
