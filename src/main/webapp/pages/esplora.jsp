<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>


    <jsp:include page="/getAuctions?action=inCorso"/>
    <c:set var="aste" value="${requestScope.aste}"/>
    <jsp:include page="/getUsers"/>
    <c:set var="utenti" value="${requestScope.utenti}"/>


    <!-- Nav Tab -->
    <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="aste-tab" data-toggle="tab" href="#auctions" role="tab" aria-controls="auctions"
               aria-selected="true">Aste</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="artisti-tab" data-toggle="tab" href="#artists" role="tab" aria-controls="artists"
               aria-selected="true">Artisti</a>
        </li>
    </ul>
    <!-- /Nav Tab -->

    <div class="tab-content" id="myTabContent">
        <!-- ASTE TAB-->
        <div class="tab-pane fade show active" id="auctions" role="tabpanel" aria-labelledby="aste-tab">

            <div class="container filtro d-flex justify-content-center align-items-center">
                <h5 class="mr-3 mb-0">Ordina per: </h5>
                <div class="dropdown">
                    <button class="dropdown-toggle btn" type="button" id="dropdownAuctions" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <div class="sort-option d-flex align-items-center">
                            <p class="mb-0 sort-text"></p>
                            <i class="fas fa-sort sort-icon"></i>
                        </div>
                    </button>

                    <span class="ml-2 invisible close-drop" style="cursor: pointer">
                        <i class="fas fa-times"></i>
                    </span>


                    <div class="dropdown-menu" aria-labelledby="dropdownAuctions">
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-cente justify-content-between">
                                <p class="mb-0">Prezzo</p>
                                <i class="fas fa-sort-amount-up"></i>
                            </div>
                        </a>

                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0">Prezzo</p>
                                <i class="fas fa-sort-amount-down"></i>
                            </div>
                        </a>

                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0 mr-3">Popolarità artista</p>
                                <i class="fas fa-sort-amount-up"></i>
                            </div>
                        </a>
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0">Popolarità artista</p>
                                <i class="fas fa-sort-amount-down"></i>
                            </div>
                        </a>
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0">Scadenza</p>
                                <i class="fas fa-sort-amount-up"></i>
                            </div>
                        </a>
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0">Scadenza</p>
                                <i class="fas fa-sort-amount-down"></i>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

            <div class="container-fluid d-flex flex-wrap" id="container-aste">

                <c:forEach var="asta" items="${aste}">
                    <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                        <div class="thumb-wrapper">
                            <div class="img-box">
                                <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}"
                                     class="img-responsive">
                            </div>
                            <div class="thumb-content">
                                <h4><c:out value="${asta.opera.nome}"/></h4>
                                <div class="expiration-timer" id="${asta.id}">
                                    <span class="timer"></span>
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
        </div>
        <!-- /ASTE TAB-->


        <!-- ARTISTI TAB-->
        <div class="tab-pane fade" id="artists" role="tabpanel" aria-labelledby="artisti-tab">

            <div class="container filtro d-flex justify-content-center align-items-center">
                <h5 class="mr-3 mb-0">Ordina per: </h5>
                <div class="dropdown">
                    <button class="dropdown-toggle btn" type="button" id="dropdownArtists" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <div class="sort-option d-flex align-items-center">
                            <p class="mb-0 sort-text"></p>
                            <i class="fas fa-sort sort-icon"></i>
                        </div>
                    </button>

                    <span class="ml-2 invisible close-drop" style="cursor: pointer">
                                <i class="fas fa-times"></i>
                            </span>


                    <div class="dropdown-menu" aria-labelledby="dropdownArtists">
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0 mr-3">Popolarità artista</p>
                                <i class="fas fa-sort-amount-up"></i>
                            </div>
                        </a>
                        <a class="dropdown-item" href="#">
                            <div class="sort-option d-flex align-items-center justify-content-between">
                                <p class="mb-0">Popolarità artista</p>
                                <i class="fas fa-sort-amount-down"></i>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

            <div class="container-fluid d-flex flex-wrap" id="container-artisti">
                <c:forEach var="utente" items="${utenti}">
                    <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                        <div class="thumb-wrapper">
                            <div class="img-box">
                                <div class="ratio img-responsive img-circle" style="background-image: url(../userPicture?id=${utente.id})">
                                </div>
                            </div>
                            <div class="thumb-content">
                                <h4><c:out value="${utente.username}"/></h4>
                                <div class="followers" id="${utente.id}">
                                    <h6 class="mb-0">Followers:</h6>
                                    <p><c:out value="${utente.nFollowers}"/></p>
                                </div>
                                <a href="#" class="btn btn-primary">Visualizza profilo</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>


        </div>
        <!-- /ARTISTI TAB-->

        <div class="modal"><!-- Place at bottom of page --></div>
    </div>






<%@include file="../static/fragments/footer.jsp" %>

