<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>


<jsp:include page="/getArtworksByUser?action=owned&id=${sessionScope.utente.id}"/>
<c:set var="operePossedute" value="${requestScope.opere}"/>


<!-- Nav Tab -->
<ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="opere-possedute-asta-tab" data-toggle="tab" href="#opere-possedute-asta" role="tab" aria-controls="opere-possedute-asta"
           aria-selected="true">Possedute</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="opere-possedute-rivendita-tab" data-toggle="tab" href="#opere-possedute-rivendita" role="tab" aria-controls="opere-possedute-rivendita"
           aria-selected="true">Create</a>
    </li>
</ul>
<!-- /Nav Tab -->

<div class="tab-content" id="myTabContent">


    <!-- Possedute TAB-->
    <div class="tab-pane fade show active" id="opere-possedute-asta" role="tabpanel" aria-labelledby="opere-possedute-asta">

        <div class="container-fluid d-flex flex-wrap" id="container-asta">
        <c:forEach var="opera" items="${operePossedute}">
            <c:if test="${opera.stato eq 'PREVENDITA'}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <div class="thumb-wrapper">
                        <div class="img-box">
                            <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}"
                                 class="img-responsive">
                        </div>
                        <div class="thumb-content">
                            <h4><c:out value="${opera.nome}"/></h4>
                            <a href="${pageContext.servletContext.contextPath}/#" class="btn btn-primary">Metti all'asta</a>

                        </div>

                    </div>

                </div>
        </c:if>
        </c:forEach>

        </div>
    </div>
    <!-- /Possedute TAB-->

    <!-- Create TAB-->
    <div class="tab-pane fade" id="opere-possedute-rivendita" role="tabpanel" aria-labelledby="opere-possedute-rivendita">

        <div class="container-fluid d-flex flex-wrap" id="container-rivendita">
        <c:forEach var="opera" items="${operePossedute}">
            <c:if test="${opera.stato eq 'IN_POSSESSO'}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <div class="thumb-wrapper">
                        <div class="img-box">
                            <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}"
                                 class="img-responsive">
                        </div>
                        <div class="thumb-content">
                            <h4><c:out value="${opera.nome}"/></h4>
                            <p>di <a href="#artista">${opera.artista.username}</a></p>
                            <p class="item-price"><c:out value="Nessuna offerta"/></p>
                            <a href="${pageContext.servletContext.contextPath}/#" class="btn btn-primary">Rivendi</a>
                        </div>

                    </div>

                </div>
            </c:if>
        </c:forEach>
        </div>
    </div>


    </div>
    <!-- /Create TAB-->

    <div class="modal"><!-- Place at bottom of page --></div>
</div>






<%@include file="../static/fragments/footer.jsp" %>

