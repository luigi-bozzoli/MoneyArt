<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>



    <c:set var="asta" value="${requestScope.asta}"/>
    <c:choose>
        <c:when test="${empty asta.partecipazioni}">
            <c:set var="bestOffer" value="${0}"/>
        </c:when>
        <c:otherwise>
            <c:set var="bestOffer" value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}"/>
        </c:otherwise>
    </c:choose>
    <script>let astaId = "${asta.id}"</script>


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
                        <p class="artist">Di <a href="${pageContext.servletContext.contextPath}/getUser?id=${asta.opera.artista.id}"><c:out value="${asta.opera.artista.username}"/></a></p>
                    </div>
                    <div class="buttons d-flex">
                        <div id="share" class="mr-3 text-center">
                            <a href="javascript:void(0)">
                                <i class="fas fa-share-alt"></i>
                                <p>Condividi</p>
                            </a>
                        </div>
                        <div id="report" class="ml-3 text-center">
                            <a style="cursor: pointer" data-toggle="modal" data-target="#segnalazioneModal">
                                <i class="fas fa-flag"></i>
                                <p>Segnala</p>
                            </a>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="segnalazioneModal" tabindex="-1" role="dialog" aria-labelledby="segnalazioneModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="segnalazioneModalLabel">Segnalazione</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <form method="post">
                                <input type="hidden" name="asta" value="${asta.id}">
                                <div class="modal-body">
                                    <textarea class="form-control" rows="3" name="commento"
                                              placeholder="Inserisci un commento"></textarea>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Chiudi</button>
                                    <button type="submit" class="btn btn-primary" style="background-color: #BB371A; border: none">Invia Segnalazione</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="content-wrapper d-flex flex-column h-100">

                    <div class="expiration-wrapper">
                        <c:choose>
                            <c:when test="${asta.stato eq 'IN_CORSO'}">
                                <h3 class="mb-3">Tempo rimanente</h3>
                            </c:when>
                            <c:when test="${asta.stato eq 'CREATA'}">
                                <h3 class="mb-3">L'asta parte fra</h3>
                            </c:when>
                            <c:when test="${asta.stato eq 'TERMINATA'}">
                                <h3 class="mb-3">L'asta è terminata</h3>
                            </c:when>

                        </c:choose>
                        <c:if test="${(asta.stato eq 'CREATA') || (asta.stato eq 'IN_CORSO')}">

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
                        </c:if>
                    </div>

                <c:if test="${(asta.stato eq 'TERMINATA') || (asta.stato eq 'IN_CORSO')}">
                    <div class="best-offer-wrapper">
                        <c:choose>
                            <c:when test="${asta.stato eq 'TERMINATA'}">
                                <h3 class="mb-3">Offerta vincente</h3
                            </c:when>
                            <c:otherwise>
                                <h3 class="mb-3">Offerta corrente</h3
                            </c:otherwise>
                        </c:choose>
                        <div class="best-offer">
                            <c:choose>
                                <c:when test="${bestOffer == 0}">
                                    <h5><c:out value="Nessuna offerta"/></h5>
                                </c:when>
                                <c:otherwise>
                                    <h5><fmt:formatNumber value="${bestOffer}" type="currency" currencySymbol="€"/></h5>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>

                <c:if test="${(asta.stato eq 'IN_CORSO') && ((empty sessionScope.utente) || (asta.opera.possessore.id != sessionScope.utente.id))}">
                    <c:choose>
                        <c:when test="${not empty requestScope.admin}">
                            <a class="text-center" id="remove-auction" href="${pageContext.servletContext.contextPath}/removeAuction?idAsta=${asta.id}">Rimuovi Asta</a>
                        </c:when>
                        <c:otherwise>
                            <div class="offer-wrapper">
                                <form method="post">
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
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </div>
        </div>
    </div>
    <div class="container-fluid details mt-3">
        <div class="col-12">
            <div class="artwork-info">
                <h2 class="artwork-title">Descrizione</h2>
                <p class="artist">Creata da <a href="${pageContext.servletContext.contextPath}/getUser?id=${asta.opera.artista.id}"><c:out value="${asta.opera.artista.username}"/></a></p>
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
        <c:if test="${not empty requestScope.admin}">
            <div class="col-12">
                <h2 class="artwork-title">Cronologia offerte</h2>
                <c:choose>
                    <c:when test="${asta.partecipazioni.size() != 0}">
                        <c:set var="size" value="${asta.partecipazioni.size()}"/>
                        <c:forEach var="i" begin="1" end="${size}" step="1">
                            <c:set var="partecipazione" value="${asta.partecipazioni[size-i]}"/>
                            <div class="d-flex w-100 justify-content-between" style="border-bottom: lightgray !important;">
                                <p>${partecipazione.utente.username}</p>
                                <p><fmt:formatNumber value="${partecipazione.offerta}" type="currency" currencySymbol="€"/></p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>Nessuna offerta per quest'asta.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

    </div>

<%@include file="../static/fragments/footer.jsp" %>


