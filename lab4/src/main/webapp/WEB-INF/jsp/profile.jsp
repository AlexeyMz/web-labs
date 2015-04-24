<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="profile.title" /> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value="profile.title"/>
    </jsp:include>
</body>
</html>
