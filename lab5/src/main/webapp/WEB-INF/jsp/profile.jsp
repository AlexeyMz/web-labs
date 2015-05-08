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
    <form class="user-profile-card" method="post" enctype="multipart/form-data">
        <img id="avatar" class="avatar" src="data:image/jpg;base64,${requestScope.avatarSrc}" />
        <div>
            <label for="firstName"><fmt:message key="profile.first_name"/>: </label>
            <input id="firstName" type="text" name="firstName" value="${requestScope.firstName}"/>
        </div>
        <div>
            <label for="defaultItemTab"><fmt:message key="profile.default_tab"/>: </label>
            <% String tab = (String)request.getAttribute("defaultItemTab"); %>
            <select id="defaultItemTab" name="defaultItemTab">
                <option value="">&mdash;</option>
                <option value="0" <%= tab.equals("0") ? "selected" : "" %>><fmt:message key="item.tab.short.caption"/></option>
                <option value="1" <%= tab.equals("1") ? "selected" : "" %>><fmt:message key="item.tab.details.caption"/></option>
                <option value="2" <%= tab.equals("2") ? "selected" : "" %>><fmt:message key="item.tab.reviews.caption"/></option>
            </select>
        </div>
        <div>
            <label for="avatarPicture"><fmt:message key="profile.avatar_picture"/>
                (&le; ${requestScope.maxAvatarSizeInKiB} KiB): </label>
            <input id="avatarPicture" type="file" name="avatarPicture"/>
            <fmt:message key="signin.submit.label" var="submitLabel" />
            <input type="submit" value="${submitLabel}" />
        </div>
    </form>
    <div class="shop-comment-panel">
        <input type="text" id="comment" placeholder="<fmt:message key="comment.placeholder" />" />
        <div id="comments"></div>
    </div>
    <script>
        var lastID = null;
        var commentRoot;

        function requestComments() {
            var param = lastID ? ("?fromID=" + (lastID + 1)) : "";
            ajax("get", "comments" + param, null, function (resultText) {
                var newComments = JSON.parse(decodeURIComponent(resultText));
                for (var i = 0; i < newComments.length; i++) {
                    var comment = newComments[i];
                    var commentElem = document.createElement("div");
                    commentElem.className = 'shop-comment';
                    commentElem.setAttribute('data-comment-id', comment.id);
                    createElement(commentElem, "div", comment.author).className = 'comment-author';
                    createElement(commentElem, "div", comment.date).className = 'comment-date';
                    createElement(commentElem, "div", comment.text).className = 'comment-text';
                    prepend(commentRoot, commentElem);
                    if (!lastID || comment.id > lastID) { lastID = comment.id; }
                }
                setTimeout(function() { requestComments(); }, 2000);
            });
        }

        function onKeyDown(e) {
            if (e.keyCode == 13) {
                ajax("post", "comments", "text=" + encodeURIComponent(this.value), function () {});
                this.value = "";
            }
        }

        document.addEventListener("DOMContentLoaded", function (event) {
            commentRoot = document.getElementById('comments');
            document.getElementById('comment').addEventListener('keydown', onKeyDown);
            requestComments();
        });
    </script>
</body>
</html>
