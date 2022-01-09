<%--
  Created by IntelliJ IDEA.
  User: aurel
  Date: 07/01/2022
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mytags" prefix="bean"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Asta</title>
</head>
<body>

    <bean:asta id="${param.id}">
        <bean:opera id="${asta.opera.id}">
            <div>
                <span>${opera.nome}</span>
            </div>
            <div>
                <p>${opera.descrizione}</p>
            </div>
        </bean:opera>
        <bean:bestOffer asta="${asta}">
            <c:if test="${bestOffer}">
                <div>
                    <span>la migliore offerta ha <fmt:formatNumber  value="${bestOffer.offerta}" type="currency"/></span>
                </div>
            </c:if>
            <c:if test="${!bestOffer}">
                <div>
                    <span>la migliore offerta ha <fmt:formatNumber  value="0.99" type="currency"/></span>
                </div
            </c:if>
        </bean:bestOffer>
        <div>
            <span>data di fine : <fmt:formatDate value="${asta.dataFine}" pattern="yy.MM.dd"></fmt:formatDate></span>
        </div>
    </bean:asta>

</body>
</html>
