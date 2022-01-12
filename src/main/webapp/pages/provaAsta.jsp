<%--
  Created by IntelliJ IDEA.
  User: aurel
  Date: 07/01/2022
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Asta</title>
</head>
<body>

    <jsp:include page="/getAuctions?action=prova" />
    <c:forEach items="${aste}" var="asta">
        <c:out value="${asta}"/>
    </c:forEach>

</body>
</html>
