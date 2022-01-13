<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var = "requestURI" value = "${pageContext.request.requestURI}"/>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- TITLE -->
    <c:choose>
        <c:when test="${fn:contains(requestURI, '/login')}">
            <title>Accedi - MoneyArt</title>
        </c:when>
        <c:when test="${fn:contains(requestURI, '/signup')}">
            <title>Registrazione - MoneyArt</title>
        </c:when>
        <c:when test="${fn:contains(requestURI, '/checkout')}">
            <title>Checkout - MoneyArt</title>
        </c:when>
        <c:when test="${fn:contains(requestURI, '/profiloUtente')}">
            <title>Profilo Utente - MoneyArt</title>
        </c:when>
        <c:when test="${fn:contains(requestURI, '/home')}">
            <title>Home - MoneyArt</title>
        </c:when>
        <c:otherwise>
            <!-- Caso con path /MoneyArt_war/ (all'avvio del server) -->
            <!-- TODO: Trovare una soluzione migliore -->
            <title>Home - MoneyArt</title>
        </c:otherwise>
    </c:choose>

    <!-- STYLESHEETS -->
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/fragments_style.css">
    <c:choose>
        <c:when test="${fn:contains(requestURI, '/login')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/signup_style.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/signup')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/signup_style.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/checkout')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/checkout_style.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/profiloUtente')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/homepage_style.css">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/profilo_utente_style.css">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/admin-lte@3.1/dist/css/adminlte.min.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/esplora')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/explore_style.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/home')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/homepage_style.css">
        </c:when>
        <c:otherwise>
            <!-- Caso con path /MoneyArt_war/ (all'avvio del server) -->
            <!-- TODO: Trovare una soluzione migliore -->
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/homepage_style.css">
        </c:otherwise>
    </c:choose>

    <!-- FAVICON -->
    <link rel="shortcut icon" href="${pageContext.servletContext.contextPath}/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${pageContext.servletContext.contextPath}/static/favicon.ico" type="image/x-icon">

    <!-- JQUERY CDN -->
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30=" crossorigin="anonymous"></script>

    <!-- BOOTSTRAP CDN -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>

    <!-- FONTAWESOME CDN -->
    <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />

    <!-- BOOTSTRAP BUNDLE -->
    <c:if test="${fn:contains(requestURI, '/profiloUtente')}">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    </c:if>

    <!-- JS -->
    <c:if test="${fn:contains(requestURI, '/signup.jsp')}">
        <script src="${pageContext.servletContext.contextPath}/static/js/signup.js"></script>
    </c:if>
    <c:if test="${fn:contains(requestURI, '/profiloUtente')}">
        <script src="https://cdn.jsdelivr.net/npm/admin-lte@3.1/dist/js/adminlte.min.js"></script>
    </c:if>
</head>
<body>
    <!-- Header -->
    <div class="content-wrapper">
        <nav class="navbar navbar-expand-xl navbar-light bg-light sticky-top" style="background-color: white !important;">
            <a class="navbar-brand" href="${pageContext.servletContext.contextPath}/pages/home.jsp">
                <img src="${pageContext.servletContext.contextPath}/static/image/logo-moneyart.png" alt="MoneyArt" srcset="">
            </a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Aste</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Artisti</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#">Marketplace</a>
                    </li>
                </ul>
                <form class="searchbar form-inline my-2 my-lg-0">
                    <input class="form-control mr-sm-2" type="search" placeholder="Cerca ..." aria-label="Search">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><i class="fas fa-search"></i></button>
                </form>
                <c:choose>
                    <c:when test="${empty sessionScope.utente}">
                        <div class="log-buttons">
                            <a href="${pageContext.servletContext.contextPath}/pages/login.jsp" class="login-button">Login</a>
                            <a href="${pageContext.servletContext.contextPath}/pages/signup.jsp" class="signup-button">Signup</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="account-buttons">
                            <a href="${pageContext.servletContext.contextPath}/pages/profiloUtente.jsp" title="Account">
                                <i class="fas fa-user-alt"></i>
                            </a>

                            <a href="${pageContext.servletContext.contextPath}/logout" title="Esci">
                                <i class="fas fa-sign-out-alt"></i>
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </div>

    <!-- Header -->