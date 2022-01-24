<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/sidebar.jsp" %>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>


<jsp:include page="/getAuctions?action="/>
<c:set var="aste" value="${requestScope.aste}"/>

<div class="page-heading">
    <h1>Aste create da te</h1>
    <span class="line-break"></span>
</div>
<div class="container">
    <!-- Nav Tab -->
    <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="aste-vinte-tab" data-toggle="tab" href="#aste-in-attesa" role="tab" aria-controls="aste-vinte"
               aria-selected="true">In attesa</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="aste-in_corso_tab" data-toggle="tab" href="#aste-in-corso" role="tab" aria-controls="aste-in-corso"
               aria-selected="true">In Corso</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="aste-perse-tab" data-toggle="tab" href="#aste-terminate" role="tab" aria-controls="aste-perse"
               aria-selected="true">Terminate</a>
        </li>
    </ul>
    <!-- /Nav Tab -->

    <div class="tab-content" id="myTabContent">
        <!-- ASTE IN ATTESA TAB-->
        <div class="tab-pane fade show active" id="aste-in-attesa" role="tabpanel" aria-labelledby="aste-vinte-tab">


            <div class="container-fluid d-flex flex-wrap" id="container-aste">

                <c:forEach var="asta" items="${aste}">
                    <c:if test="${(asta.opera.artista.id == sessionScope.utente.id) && (asta.stato eq 'CREATA')}">
                        <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3" id = "in-attesa-${asta.id}">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${asta.opera.id}"
                                         class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4><c:out value="${asta.opera.nome}"/></h4>
                                    <div class="expiration-timer" id="${asta.id}">
                                        <span class="timer"><fmt:formatDate pattern="MM dd yyyy" value="${asta.dataInizio}" /></span>
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
                                    <button value="${asta.id}" href="" class="bottone-in-attesa btn btn-primary">Elimina</button>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!-- /ASTE IN ATTESA TAB-->




        <!-- /ASTE IN CORSO TAB-->
        <div class="tab-pane fade" id="aste-in-corso" role="tabpanel" aria-labelledby="aste-in_corso_tab">


            <div class="container-fluid d-flex flex-wrap" id="container-aste-in-corso">
                <c:forEach var="asta" items="${aste}">
                    <c:if test="${(asta.opera.artista.id == sessionScope.utente.id) && (asta.stato eq 'IN_CORSO')}">
                        <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3" id = "in-corso-${asta.id}">
                            <div class="thumb-wrapper" >
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
                                    <button value="${asta.id}" href="" class="bottone-in-corso btn btn-primary">Elimina</button>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!-- /ASTE IN CORSO TAB-->

        <!-- ASTE TERMINATE TAB-->
        <div class="tab-pane fade" id="aste-terminate" role="tabpanel" aria-labelledby="aste-perse-tab">


            <div class="container-fluid d-flex flex-wrap" id="container-aste-perse">
                <c:forEach var="asta" items="${aste}">
                    <c:if test="${(asta.opera.artista.id == sessionScope.utente.id) && (asta.stato eq 'TERMINATA')}">
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
                    </c:if>
                </c:forEach>
            </div>
        </div>
        <!-- /ASTE TERMINATE TAB-->

        <div class="modal"><!-- Place at bottom of page --></div>
    </div>
</div>









<%@include file="../static/fragments/footer.jsp" %>

