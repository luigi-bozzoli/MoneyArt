<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../static/fragments/header.jsp"%>
    <div class="page-heading">
        <h1>Accedi</h1>
        <span class="line-break"></span>
    </div>

    <div class="container">
        <div class="col-12">
            <div class="error">
                <c:if test="${not empty requestScope.error}">
                    <p class="text-center">${requestScope.error}</p>
                </c:if>
            </div>
        </div>
    </div>

    <div class="signup-box">
        <img class="signup-logo" src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/logo-moneyart.png" alt="Logo MoneyArt">
        <form class="signup" method="post" name="login" action="<c:out value="${pageContext.servletContext.contextPath}"/>/login">
            <div class="signup-input">
                <label for="username">Username - Email</label>
                <div class="input-icon">
                    <i class="fas fa-at"></i>
                    <input name="username" id="username" type="text" placeholder="JohnDoe99">
                </div>
            </div>


            <div class="signup-input">
                <label for="password">Password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="password" id="password" type="password" placeholder="Password">
                </div>
            </div>
            <button type="submit">Login</button>
        </form>
    </div>
<%@include file="../static/fragments/footer.jsp"%>
