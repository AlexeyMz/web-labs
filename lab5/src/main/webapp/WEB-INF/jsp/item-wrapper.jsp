<%@ page import="java.util.ResourceBundle" %>
<%@ page import="ru.alexeymz.web.core.template.ExpressionEvaluator" %>
<%@ page import="ru.alexeymz.web.core.template.ViewTemplate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <% ResourceBundle l10n = (ResourceBundle)request.getAttribute("l10n"); %>
    <% ViewTemplate pageTemplate = (ViewTemplate)request.getAttribute("template"); %>
    <% ExpressionEvaluator evaluator = (ExpressionEvaluator)request.getAttribute("expressionEval"); %>
    <meta charset="UTF-8">
    <title><%= evaluator.evaluate("item.name") %> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value="store.title"/>
    </jsp:include>
    <%= pageTemplate.render(l10n, evaluator) %>
</body>
</html>
