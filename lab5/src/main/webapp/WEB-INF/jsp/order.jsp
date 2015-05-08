<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.langCode}" />
<fmt:setBundle basename="i18n/text" />
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="order.title" /> | <fmt:message key="store.title" /></title>
    <link rel="stylesheet" type="text/css" href="css/main.css" />
    <script src="//api-maps.yandex.ru/2.1/?lang=ru_RU" type="text/javascript"></script>
    <script src="js/util.js"></script>
</head>
<body>
    <jsp:include page="topbar.jsp">
        <jsp:param name="title-key" value="order.title"/>
    </jsp:include>
    <form class="order-form" action="orders" method="post">
        <div id="map" class="order-map"></div>
        <ul>
        <c:forEach var="entry" items="${sessionScope.entries}">
            <li>[<a href="item?id=${entry.card.id}"><c:out value="${entry.card.id}"/></a>]
                <c:out value="${entry.card.name}"/>
                (<fmt:formatNumber value="${entry.card.price}" minFractionDigits="2" type="currency" currencyCode="USD" />)
                x <c:out value="${entry.quantity}" />
            </li>
        </c:forEach>
        </ul>
        <div class="total-price-info"><span><fmt:message key="cart.total" /></span>:
            <fmt:formatNumber value="${sessionScope.total}" minFractionDigits="2" type="currency" currencyCode="USD" />
        </div>
        <label><fmt:message key="order.delivery_type" />: </label>
        <input type="radio" name="deliveryType" value="toDeliveryPoint" checked="checked" onchange="onDeliveryKindChanged()" />
        <fmt:message key="order.self_delivery_type" />
        <input type="radio" name="deliveryType" value="byCourier" onchange="onDeliveryKindChanged()"/>
        <fmt:message key="order.delivery_by_courier" /><br>
        <div class="self-delivery-selection">
            <label for="deliveryPointCombo"><fmt:message key="order.delivery_point" />: </label>
            <select id="deliveryPointCombo" name="deliveryPoint" onchange="updateMarkSelection(marks[this.selectedIndex].deliveryKey);">
                <c:forEach var="point" items="${requestScope.points}">
                    <option value="${point.key}">${point.address}</option>
                </c:forEach>
            </select>
        </div>
        <div class="courier-delivery-address">
            <label for="deliveryAddressText"><fmt:message key="order.delivery_address" />: </label>
            <input id="deliveryAddressText" type="text" name="deliveryAddress"
                   placeholder="<fmt:message key="order.delivery_address.placeholder" />" />
        </div>
        <input type="submit" value="<fmt:message key="order.submit_order" />" />
    </form>
    <script>
        ymaps.ready(init);
        var myMap;
        var marks = [];

        function init() {
            myMap = new ymaps.Map("map", {
                center: [59.93, 30.31],
                zoom: 9
            });

            var mark;
            <c:forEach var="point" items="${requestScope.points}">
                mark = new ymaps.Placemark([${point.coordinates}], {
                    //balloonContent: '<strong><fmt:message key="order.map.address" /></strong>: ' + '${point.address}'
                }, {preset: 'islands#dotIcon', iconColor: '#3b5998'});
                mark.deliveryKey = '${point.key}';
                mark.deliveryAddress = '${point.address}';
                myMap.geoObjects.add(mark);
                marks.push(mark);
            </c:forEach>

            myMap.geoObjects.events.add('click', function (e) {
                var object = e.get('target');
                document.querySelector("input[value='toDeliveryPoint']").checked = true;
                document.querySelector("input[value='byCourier']").checked = false;
                updateComboSelection(object.deliveryKey);
                updateMarkSelection(object.deliveryKey);
            });

            var selectedMarkIndex = document.getElementById('deliveryPointCombo').selectedIndex;
            updateMarkSelection(marks[selectedMarkIndex].deliveryKey);
        }

        function updateComboSelection(selectedKey) {
            var deliveryOptions = document.querySelectorAll("#deliveryPointCombo option");
            for (var i = 0; i < deliveryOptions.length; i++) {
                var opt = deliveryOptions[i];
                if (opt.value === selectedKey) {
                    document.getElementById('deliveryPointCombo').selectedIndex = i;
                    break;
                }
            }
        }

        function updateMarkSelection(selectedKey) {
            for (var j = 0; j < marks.length; j++) {
                var obj = marks[j];
                if (obj.deliveryKey === selectedKey) {
                    obj.options.set('iconColor', '#ff00ff');
                } else {
                    obj.options.set('iconColor', '#3b5998');
                }
            }
        }

        function onDeliveryKindChanged() {
            var combo = document.getElementById('deliveryPointCombo');
            var selfDeliveryCheckbox = document.querySelector("input[value='toDeliveryPoint']");
            updateMarkSelection(selfDeliveryCheckbox.checked
                    ? marks[combo.selectedIndex].deliveryKey : null);
        }
    </script>
</body>
</html>
