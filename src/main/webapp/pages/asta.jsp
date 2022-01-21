<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>


    <c:set var="asta" value="${requestScope.asta}"/>
    <c:set var="bestOffer" value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}"/>

    <div class="container-fluid asta-container d-flex flex-wrap mb-3">
        <div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
            <div class="img-wrapper d-flex justify-content-center">
                <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}" class="img-responsive">
            </div>
        </div>
        <div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
            <div class="info-wrapper d-flex flex-column justify-content-between h-100">
                <div class="info-heading d-flex justify-content-between flex-wrap">
                    <div class="artwork-info">
                        <h2 class="artwork-title"><c:out value="${asta.opera.nome}"/></h2>
                        <p class="artist">Di <a href="#"><c:out value="${asta.opera.artista.username}"/></a></p>
                    </div>
                    <div class="buttons d-flex">
                        <div id="share" class="mr-3 text-center">
                            <a href="#">
                                <i class="fas fa-share-alt"></i>
                                <p>Condividi</p>
                            </a>
                        </div>
                        <div id="report" class="ml-3 text-center">
                            <a href="#">
                                <i class="fas fa-flag"></i>
                                <p>Segnala</p>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="content-wrapper d-flex flex-column h-100">
                    <div class="expiration-wrapper">
                        <h3 class="mb-3">Tempo rimanente</h3>
                        <div class="timer-info d-flex justify-content-between ml-1">
                            <div class="days">
                                <h5>--</h5>
                                <span>Giorni</span>
                            </div>
                            <div class="hours">
                                <h5>--</h5>
                                <span>Ore</span>
                            </div>
                            <div class="minutes">
                                <h5>--</h5>
                                <span>Minuti</span>
                            </div>
                            <div class="seconds">
                                <h5>--</h5>
                                <span>Secondi</span>
                            </div>

                        </div>
                    </div>

                    <div class="best-offer-wrapper">
                        <h3 class="mb-3">Offerta corrente</h3>
                        <div class="best-offer">
                            <h5><fmt:formatNumber value="${bestOffer}" type="currency" currencySymbol="€"/></h5>
                        </div>
                    </div>

                    <div class="offer-wrapper">
                        <form action="${pageContext.servletContext.contextPath}/newOffer" method="post">

                            <label for="offerta"><h3>Fai un'offerta</h3></label>
                            <div class="offer-input">
                                <input type="hidden" name="asta" value="${asta.id}">
                                <input type="text" name="offerta" id="offerta" placeholder="<fmt:formatNumber value="${bestOffer+0.01}" type="currency" currencySymbol="€"/>" class="mr-3">
                                <button type="submit" class="disabled">Offri</button>
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
    </div>
    <div class="container-fluid details mt-3">
        <div class="col-12">
            <div class="artwork-info">
                <h2 class="artwork-title">Descrizione</h2>
                <p class="artist">Creata da <a href="#"><c:out value="${asta.opera.artista.username}"/></a></p>
            </div>

            <div class="description">
                <c:choose>
                    <c:when test="${empty asta.opera.descrizione}">
                        Nessuna descrizione.
                    </c:when>
                    <c:otherwise>
                        <c:out value="${asta.opera.descrizione}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

<script>let astaId = "${asta.id}"</script>

<%@include file="../static/fragments/footer.jsp" %>


