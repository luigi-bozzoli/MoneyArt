<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@include file="../static/fragments/header.jsp" %>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>


<jsp:include page="/userAuctions?action=won"/>
<c:set var="vinte" value="${requestScope.asteVinte}"/>
<jsp:include page="/userAuctions?action=lost"/>
<c:set var="perse" value="${requestScope.astePerse}"/>
<jsp:include page="/userAuctions?action=current"/>
<c:set var="inCorso" value="${requestScope.asteInCorso}"/>


<!-- Nav Tab -->
<ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="aste-vinte-tab" data-toggle="tab" href="#aste-vinte" role="tab" aria-controls="aste-vinte"
           aria-selected="true">Vinte</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="aste-in_corso_tab" data-toggle="tab" href="#aste-in-corso" role="tab" aria-controls="aste-in-corso"
           aria-selected="true">In Corso</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="aste-perse-tab" data-toggle="tab" href="#aste-perse" role="tab" aria-controls="aste-perse"
           aria-selected="true">Perse</a>
    </li>
</ul>
<!-- /Nav Tab -->

<div class="tab-content" id="myTabContent">
    <!-- ASTE VINTE TAB-->
    <div class="tab-pane fade show active" id="aste-vinte" role="tabpanel" aria-labelledby="aste-vinte-tab">


        <div class="container-fluid d-flex flex-wrap" id="container-aste">

            <c:forEach var="asta" items="${vinte}">
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

                            <a href="#" class="btn btn-primary">Vai all'asta</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <!-- /ASTE VINTE TAB-->




    <!-- /ASTE IN CORSO TAB-->
    <div class="tab-pane fade" id="aste-in-corso" role="tabpanel" aria-labelledby="aste-in_corso_tab">


        <div class="container-fluid d-flex flex-wrap" id="container-aste-in-corso">
            <c:forEach var="asta" items="${inCorso}">
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

                            <a href="#" class="btn btn-primary">Vai all'asta</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <!-- /ASTE IN CORSO TAB-->

    <!-- /ASTE ASTE PERSE TAB-->
    <div class="tab-pane fade" id="aste-perse" role="tabpanel" aria-labelledby="aste-perse-tab">


        <div class="container-fluid d-flex flex-wrap" id="container-aste-perse">
            <c:forEach var="asta" items="${perse}">
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

                            <a href="#" class="btn btn-primary">Vai all'asta</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <!-- /ASTE PERSE TAB-->

    <div class="modal"><!-- Place at bottom of page --></div>
</div>






<%@include file="../static/fragments/footer.jsp" %>

