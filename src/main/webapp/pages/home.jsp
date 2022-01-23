<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp"%>

    <jsp:include page="/getAuctions?action=inCorso"/>
    <c:set var="aste" value="${requestScope.aste}"/>

    <!-- HERO SECTION -->
    <div class="row hero">
        <div class="col left-hero">
            <h1>L'arte che diventa<br>
                <span class="inverted-capital">N</span>on
                <span class="inverted-capital">F</span>ungible
                <span class="inverted-capital">T</span>oken</h1>
            <div class="hero-buttons">
                <a href="${pageContext.servletContext.contextPath}/pages/esplora.jsp" class="button">Esplora</a>
                <c:choose>
                    <c:when test="${empty sessionScope.utente}">
                        <a href="${pageContext.servletContext.contextPath}/pages/login.jsp" class="button">Crea</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.servletContext.contextPath}/pages/creaOpera.jsp" class="button">Crea</a>  <!-- todo aggiungere link alla creazione -->
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="col right-hero" style="padding-right: 30px">
            <img class="hero-image" src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/the-last-supper.jpg" width="800px" alt="The Last Supper" style="border-radius: 50px;">
        </div>
    </div>

    <img class="wave" src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/wave.svg">

    <div class="container auction-carousel large" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-xl" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-xl" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-xl" data-slide-to="1"></li>
            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <c:forEach var="asta" items="${aste}" begin="0" end="2">
                            <div class="col-md-4">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="2" end="5">
                            <div class="col-md-4">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Second Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-xl" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-xl" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>

    <div class="container auction-carousel medium" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-md" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-md" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-md" data-slide-to="1"></li>
                <li data-target="#carousel-md" data-slide-to="2"></li>
            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <c:forEach var="asta" items="${aste}" begin="0" end="1">
                            <div class="col-md-6">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="2" end="3">
                            <div class="col-md-6">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Second Slide-->
                    <!-- Third Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="4" end="5">
                            <div class="col-md-6">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Third Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-md" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-md" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>

    <div class="container auction-carousel small" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-sd" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-sd" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-sd" data-slide-to="1"></li>
                <li data-target="#carousel-sd" data-slide-to="2"></li>
                <li data-target="#carousel-sd" data-slide-to="3"></li>
                <li data-target="#carousel-sd" data-slide-to="4"></li>
                <li data-target="#carousel-sd" data-slide-to="5"></li>

            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <c:forEach var="asta" items="${aste}" begin="0" end="0">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="1" end="1">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Second Slide-->
                    <!-- Third Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="2" end="2">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Third Slide-->
                    <!-- Fourth Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="3" end="3">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Fourth Slide-->
                    <!-- Fifth Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="4" end="4">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Sixth Slide-->
                    <div class="carousel-item">
                        <c:forEach var="asta" items="${aste}" begin="5" end="5">
                            <div class="col-sm-12">
                                <div class="thumb-wrapper">
                                    <div class="img-box">
                                        <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
                                    </div>
                                    <div class="thumb-content">
                                        <h4>${asta.opera.nome}</h4>
                                        <div class="expiration-timer">
                                            <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataFine}" /></span>
                                        </div>
                                        <c:choose>
                                            <c:when test="${empty asta.partecipazioni}">
                                                <p class="item-price"><c:out value="Nessuna offerta"/></p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency" currencySymbol="€"/></p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.servletContext.contextPath}/getAuction?id=${asta.id}" class="btn btn-primary">Vai all'asta</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <!-- /Sixth Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-sd" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-sd" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>

    <div class="container">
        <div class="page-heading">
            <h2>Cosa sono gli NFT?</h2>
            <span class="line-break"></span>
        </div>
        <!-- 16:9 aspect ratio -->
        <div class="embed-responsive embed-responsive-16by9">
            <iframe class="embed-responsive-item" src="https://www.youtube.com/embed/FkUn86bH34M"></iframe>
        </div>
    </div>





<%@include file="../static/fragments/footer.jsp"%>
