<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="signin.title" /> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value="signin.title"/>
    </jsp:include>
    <fmt:message key="signin.username.placeholder" var="usernamePlaceholder" />
    <fmt:message key="signin.submit.label" var="submitLabel" />
    <form class="login-form" action="j_security_check" method="post" name="loginForm">
        <c:if test="${requestScope.authError}">
            <div class="auth-error"><fmt:message key="signin.error"/></div>
        </c:if>
        <div>
            <label><fmt:message key="signin.username.label" />: </label>
            <input type="text" name="j_username" placeholder="${usernamePlaceholder}"/>
        </div>
        <div>
            <label><fmt:message key="signin.password.label" />: </label>
            <input type="password" name="j_password"/>
        </div>
        <input type="submit" value="${submitLabel}"/>
    </form>
</body>
</html>
