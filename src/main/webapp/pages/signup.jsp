<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<html lang="it">
<head>
    <title>Registrazione - MoneyArt</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/signup_style.css">
    <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/fragments_style.css">

    <!-- FAVICON -->
    <link rel="shortcut icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">

    <!-- JQUERY CDN -->
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30=" crossorigin="anonymous"></script>

    <!-- BOOTSTRAP CDN -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>



    <!-- FONTAWESOME CDN -->
    <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />

    <!-- JS -->
    <script src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/js/signup.js"></script>
    
</head>
<body>
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
                    <input name="email" id="email" type="email" placeholder="yourmail@mail.com" required>
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




</body>
</html>
