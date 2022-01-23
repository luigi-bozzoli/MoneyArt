<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>



<jsp:include page="/searchVendite?action=${param.action}"/>
<c:set var="aste" value="${requestScope.aste}"/>
<c:set var="rivendite" value="${requestScope.rivendite}"/>


<!-- Nav Tab -->
<ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="aste-tab" data-toggle="tab" href="#auctions" role="tab" aria-controls="auctions"
           aria-selected="true">All'asta</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="artisti-tab" data-toggle="tab" href="#artists" role="tab" aria-controls="artists"
           aria-selected="true">In Vendita</a>
    </li>
</ul>
<!-- /Nav Tab -->

<div class="tab-content" id="myTabContent">
    <!-- ASTE TAB-->
    <div class="tab-pane fade show active" id="auctions" role="tabpanel" aria-labelledby="aste-tab">

        <div class="container-fluid d-flex flex-wrap" id="container-aste-in-corso">
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

    </div>
    <!-- /ASTE TAB-->


    <!-- RIVENDITE TAB-->
    <div class="tab-pane fade" id="artists" role="tabpanel" aria-labelledby="artisti-tab">

        <div class="container-fluid d-flex flex-wrap" id="container-rivendite">
            <c:forEach var="rivendita" items="${rivendite}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <div class="thumb-wrapper">
                        <div class="img-box">
                            <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${rivendita.opera.id}"
                                 class="img-responsive">
                        </div>
                        <div class="thumb-content">
                            <h4><c:out value="${rivendita.opera.nome}"/></h4>

                            <h6>Di ${rivendita.opera.artista.username}</h6>

                            <p class="item-price"><fmt:formatNumber value="${rivendita.prezzo}" type="currency" currencySymbol="€"/></p>

                            <a href="${pageContext.servletContext.contextPath}/getResell?id=${rivendita.id}" class="btn btn-primary">Acquista</a>
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


