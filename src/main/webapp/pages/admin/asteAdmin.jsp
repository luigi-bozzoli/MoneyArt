<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../../static/fragments/header.jsp" %>

    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>

    <jsp:include page="/getAuctions?action=inCorso"/>
    <c:set var="aste" value="${requestScope.aste}"/>

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
                        <a href="${pageContext.servletContext.contextPath}/removeAuction?idAsta=${asta.id}" class="btn btn-primary">Rimuovi Asta</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

<%@include file="../../static/fragments/footer.jsp" %>