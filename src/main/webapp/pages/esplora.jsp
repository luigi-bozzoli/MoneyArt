<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@include file="../static/fragments/header.jsp" %>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>


    <jsp:include page="/getAuctions?action=inCorso"/>
    <c:set var="aste" value="${requestScope.aste}"/>

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
        <div class="tab-pane fade show active" id="auctions" role="tabpanel" aria-labelledby="aste-tab">

                    <div class="container filtro d-flex justify-content-center align-items-center">
                        <h5 class="mr-3 mb-0">Ordina per: </h5>
                        <div class="dropdown">
                            <button class="dropdown-toggle btn" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <div class="sort-option d-flex align-items-center">
                                    <p class="mb-0" id="sort-text"></p>
                                    <i id="sort-icon" class="fas fa-sort"></i>
                                </div>
                            </button>

                            <span id="close-drop" class="ml-2 invisible" style="cursor: pointer">
                                <i class="fas fa-times"></i>
                            </span>


                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
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
                                        <p class="item-price"><fmt:formatNumber value="${asta.partecipazioni.get(asta.partecipazioni.size()-1).offerta}" type="currency"/></p>
                                    </c:otherwise>
                                </c:choose>

                                <a href="#" class="btn btn-primary">Vai all'asta</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="tab-pane fade" id="artists" role="tabpanel" aria-labelledby="artisti-tab">
            ARTISTIIIII
        </div>

        <div class="modal"><!-- Place at bottom of page --></div>
    </div>






<%@include file="../static/fragments/footer.jsp" %>

