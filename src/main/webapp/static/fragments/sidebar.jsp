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
    <title>Profilo Utente - MoneyArt</title>

    <!-- STYLESHEETS -->
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/fragments_style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/admin-lte@3.1/dist/css/adminlte.min.css">

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


    <c:choose>
        <c:when test="${fn:contains(requestURI, '/profiloUtente')}">
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/profilo_utente_style.css">
        </c:when>
        <c:when test="${fn:contains(requestURI, '/wallet')}">
            <!-- JQUERY CDN -->
            <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30=" crossorigin="anonymous"></script>

            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/js/bootstrap.bundle.min.js"></script>
            <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css">
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
            <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/static/style/wallet.css">
        </c:when>
    </c:choose>

    <!-- JS -->
        <script src="${pageContext.servletContext.contextPath}/static/js/sidebar.js"></script>
        <c:choose>
            <c:when test="${fn:contains(requestURI, '/profiloUtente')}">
                <script src="${pageContext.servletContext.contextPath}/static/js/updateProfile.js"></script>
            </c:when>
        </c:choose>

</head>

<body class="sidebar-mini layout-fixed sidebar-collapse" style="height: auto;">
    <div class="wrapper layout-top-nav sticky-top">
        <nav class="main-header navbar navbar-expand navbar-white navbar-light">
            <!-- Left navbar links -->
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-minus-square"></i></a>
                </li>
                <li>
                    <a class="nav-link" href="${pageContext.servletContext.contextPath}/pages/home.jsp"><i class="fas fa-home"></i></a>
                </li>
            </ul>
        </nav>
        <aside class="main-sidebar sidebar-dark-primary elevation-4">
            <!-- Sidebar -->
            <div class="sidebar os-host os-theme-light os-host-overflow os-host-overflow-y os-host-resize-disabled os-host-transition os-host-scrollbar-horizontal-hidden">
                <div class="os-resize-observer-host observed">
                    <div class="os-resize-observer" style="left: 0px; right: auto;"></div>
                </div>
                <div class="os-size-auto-observer observed" style="height: calc(100% + 1px); float: left;">
                    <div class="os-resize-observer"></div>
                </div>
                <div class="os-padding">
                    <div class="os-viewport os-viewport-native-scrollbars-invisible">
                        <div class="os-content" style="padding: 0px 8px; height: 100%; width: 100%;">
                            <!-- Sidebar Menu -->
                            <nav class="mt-2">
                                <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
                                    <!-- Add icons to the links using the .nav-icon class
                               with font-awesome or any other icon font library -->
                                    <li class="nav-item">
                                        <a href="${pageContext.servletContext.contextPath}/pages/profiloUtente.jsp" class="nav-link">
                                            <i class="nav-icon fas fa-user"></i>
                                            <p>Profilo</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#" class="nav-link">
                                            <i class="nav-icon fas fa-download"></i>
                                            <p>Carica opera</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="#" class="nav-link">
                                            <i class="nav-icon fas fa-folder"></i>
                                            <p>Le mie opere</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="${pageContext.servletContext.contextPath}/pages/wallet.jsp" class="nav-link">
                                            <i class="nav-icon fas fa-wallet"></i>
                                            <p>Wallet</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="${pageContext.servletContext.contextPath}/pages/asteUtente.jsp" class="nav-link">
                                            <i class="nav-icon fas fa-th-list"></i>
                                            <p>Aste</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="${pageContext.servletContext.contextPath}/pages/notifiche.jsp" class="nav-link">
                                            <i class="nav-icon fas fa-bell"></i>
                                            <p>Notifiche</p>
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a href="/logout" class="nav-link">
                                            <i class="nav-icon fas fa-sign-out-alt"></i>
                                            <p>LogOut</p>
                                        </a>
                                    </li>
                                </ul>
                            </nav>
                            <!-- /.sidebar-menu -->
                        </div>
                    </div>
                </div>
                <div class="os-scrollbar os-scrollbar-horizontal os-scrollbar-unusable os-scrollbar-auto-hidden">
                    <div class="os-scrollbar-track">
                        <div class="os-scrollbar-handle" style="width: 100%; transform: translate(0px, 0px);"></div>
                    </div>
                </div>
                <div class="os-scrollbar os-scrollbar-vertical os-scrollbar-auto-hidden">
                    <div class="os-scrollbar-track">
                        <div class="os-scrollbar-handle" style="height: 29.9017%; transform: translate(0px, 0px);"></div>
                    </div>
                </div>
                <div class="os-scrollbar-corner"></div>
            </div>
            <!-- /.sidebar -->
        </aside>
    </div>
</body>