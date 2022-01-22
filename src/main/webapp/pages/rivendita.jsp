<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>


    <c:set var="rivendita" value="${requestScope.rivendita}"/>
    <script>let rivenditaId = "${rivendita.id}"</script>


    <div class="container-fluid asta-container d-flex flex-wrap mb-3">
        <div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
            <div class="img-wrapper d-flex justify-content-center">
                <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${rivendita.opera.id}" class="img-responsive">
            </div>
        </div>
        <div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
            <div class="info-wrapper d-flex flex-column justify-content-between h-100">
                <div class="info-heading d-flex justify-content-between flex-wrap">
                    <div class="artwork-info">
                        <h2 class="artwork-title"><c:out value="${rivendita.opera.nome}"/></h2>
                        <p class="artist">Di <a href="#"><c:out value="${rivendita.opera.possessore.username}"/></a></p>
                    </div>
                    <div class="buttons d-flex">
                        <div id="share" class="mr-3 text-center">
                            <a href="javascript:void(0)">
                                <i class="fas fa-share-alt"></i>
                                <p>Condividi</p>
                            </a>
                        </div>
                        <div id="report" class="ml-3 text-center">
                            <a href="javascript:void(0)">
                                <i class="fas fa-flag"></i>
                                <p>Segnala</p>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="content-wrapper d-flex flex-column h-100">

                    <div class="description">
                        <div class="artwork-info">
                            <h3>Descrizione</h3>
                            <p class="artist">Creata da <a href="#"><c:out value="${rivendita.opera.artista.username}"/></a></p>
                        </div>
                        <c:choose>
                            <c:when test="${empty rivendita.opera.descrizione}">
                                Nessuna descrizione.
                            </c:when>
                            <c:otherwise>
                                <c:out value="${rivendita.opera.descrizione}"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="best-offer-wrapper">
                        <h3 class="mb-3">Prezzo</h3>
                        <div class="best-offer">
                            <h5><fmt:formatNumber value="${rivendita.prezzo}" type="currency" currencySymbol="€"/></h5>
                        </div>
                </div>

                <div class="offer-wrapper">
                    <form action="${pageContext.servletContext.contextPath}/buyArtwork" method="post">
                        <label for="buy"><h3>Acquisto diretto</h3></label>
                        <div class="offer-input">
                            <input type="hidden" name="asta" value="${rivendita.id}">
                            <button type="submit" id="buy">Compra</button>
                        </div>
                        <div class="error">
                            <c:if test="${not empty requestScope.error}">
                                <p class="mt-3" style="color: #BB371A !important;">${requestScope.error}</p>
                            </c:if>
                        </div>
                        <div class="message">
                            <c:if test="${not empty requestScope.message}">
                                <p class="mt-3" style="color: #05cf48 !important;">${requestScope.message}</p>
                            </c:if>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid details mt-3">
        <div class="col-12">

        </div>
    </div>



<%@include file="../static/fragments/footer.jsp" %>



