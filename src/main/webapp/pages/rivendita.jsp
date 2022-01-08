<%--
  Created by IntelliJ IDEA.
  User: Xzeni
  Date: 1/8/2022
  Time: 4:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="mytags" prefix="bean"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Rivendita</title>
</head>
<body>
<bean:rivendita id="${param.id}">
    <bean:opera id="${rivendita.opera.id}">
        <div>
            <span>${opera.nome}</span>
        </div>
        <div>
            <p>${opera.descrizione}</p>
        </div>
    </bean:opera>
    <div>
        <span>Prezzo : ${rivendita.prezzo}</span>
    </div>

    <div>
        <span><button>Acquista Ora</button></span>
    </div>
</bean:rivendita>

</body>
</html>
