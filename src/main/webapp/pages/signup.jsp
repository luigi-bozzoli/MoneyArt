<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../static/fragments/header.jsp"%>
    <div class="page-heading">
        <h1>Crea un nuovo account</h1>
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
        <!-- Form -->
        <form class="signup" method="post" name="signup" action="<c:out value="${pageContext.servletContext.contextPath}"/>/signup">
            <div class="signup-input">
                <label for="name">Nome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="name" id="name" type="text" placeholder="John" required>
                </div>
            </div>

            <div class="signup-input">
                <label for="surname">Cognome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="surname" id="surname" type="text" placeholder="Doe" required>
                </div>
            </div>

            <div class="signup-input">
                <label for="email">Email</label>
                <div class="input-icon">
                    <i class="fas fa-envelope"></i>
                    <input name="email" id="email" type="text" placeholder="yourmail@mail.com" required>
                </div>
            </div>

            <div class="signup-input">
                <label for="username">Username</label>
                <div class="input-icon">
                    <i class="fas fa-at"></i>
                    <input name="username" id="username" type="text" placeholder="JohnDoe99" requirede>
                </div>
            </div>

            <div class="signup-input pw">
                <label for="password">Password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="password" id="password" type="password" placeholder="Password" required>
                </div>
            </div>

            <div class="signup-input pw">
                <label for="repeat-password">Ripeti password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="repeat-password" id="repeat-password" type="password" placeholder="Repeat password" required>
                </div>
            </div>

            <button type="submit">Registrati</button>
        </form>
        <!-- /Form -->
    </div>
<%@include file="../static/fragments/footer.jsp"%>
