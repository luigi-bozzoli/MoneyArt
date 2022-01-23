<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>




    <c:set var="artista" value="${requestScope.artista}"/>
    <c:set var="utente" value="${sessionScope.utente}"/>

    <div class="container">
        <div class="img-box mt-3 ml-auto mr-auto">
            <div class="ratio img-responsive img-circle" style="background-image: url(./userPicture?id=${artista.id})">
            </div>
        </div>
        <div class="d-flex flex-column align-items-center mt-3">
            <h2><c:out value="${artista.username}"/></h2>
            <div class="d-flex flex-row align-items-baseline">
                <h4 class="mb-3 mr-3">Followers:</h4>
                <h5 style="color: #eba83a !important;"><c:out value="${artista.nFollowers}"/></h5>
            </div>
            <div class="d-flex flex-row align-items-baseline">
                <h4 class="mb-3 mr-3">Opere create:</h4>
                <h5 style="color: #eba83a !important;"><c:out value="${artista.opereCreate.size()}"/></h5>
            </div>
            <div class="following">
                <c:choose>

                    <c:when test="${empty utente}">
                        <div class="follow d-flex flex-column align-items-center">
                            <a href="javascript:void(0)" class="btn disabled mb-3">Segui</a>
                            <p style="color: #BB371A !important;">Devi effettuare l'accesso per seguire questo artista!</p>
                        </div>
                    </c:when>
                    <c:when test="${not empty utente && (utente.id == artista.id) }">
                        <div class="follow d-flex flex-column align-items-center">
                            <a href="javascript:void(0)" class="btn disabled mb-3">Segui</a>
                            <p style="color: #BB371A !important;">Non puoi seguire te stesso!</p>
                        </div>
                    </c:when>
                    <c:when test="${not empty utente && empty utente.seguito.id}">
                        <div class="follow d-flex flex-column align-items-center">
                            <a href="javascript:void(0)" class="btn">Segui</a>
                        </div>
                    </c:when>
                    <c:when test="${not empty utente && utente.seguito.id == artista.id}">
                        <div class="unfollow d-flex flex-column align-items-center">
                            <a href="javascript:void(0)" class="btn">Smetti di seguire</a>
                        </div>
                    </c:when>
                    <c:when test="${not empty utente && not empty utente.seguito.id && utente.seguito.id != artista.id}">
                        <div class="follow d-flex flex-column align-items-center">
                            <a href="javascript:void(0)" class="btn mb-3">Segui</a>
                            <p style="color: #BB371A !important;">Se seguirai ${artista.username}, smetterai di seguire ${utente.seguito.username} automaticamente</p>
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>

        <div class="page-heading">
            <h1>Opere di ${artista.username}</h1>
            <span class="line-break"></span>
        </div>
    </div>
    <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
        <li class="nav-item">
            <a class="nav-link" id="possedute-tab" data-toggle="tab" href="#possedute" role="tab" aria-controls="auctions" aria-selected="true">Possedute</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active" id="create-tab" data-toggle="tab" href="#create" role="tab" aria-controls="artists" aria-selected="true">Create</a>
        </li>
    </ul>

<div class="tab-content" id="myTabContent">
    <div class="tab-pane fade show active" id="possedute" role="tabpanel" aria-labelledby="possedute-tab">
        <div class="container-fluid d-flex flex-wrap" id="container-aste">
            <c:forEach var="opera" items="${artista.opereInPossesso}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <div class="thumb-wrapper">
                        <div class="img-box artwork">
                            <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}"
                                 class="img-responsive">
                        </div>
                        <div class="thumb-content">
                            <h4><c:out value="${opera.nome}"/></h4>
                            <h6>Creato da ${opera.artista.username}</h6>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="tab-pane fade show" id="create" role="tabpanel" aria-labelledby="create-tab">
        <div class="container-fluid d-flex flex-wrap">

            <c:forEach var="opera" items="${artista.opereCreate}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <div class="thumb-wrapper">
                        <div class="img-box artwork">
                            <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}"
                                 class="img-responsive">
                        </div>
                        <div class="thumb-content">
                            <h4><c:out value="${opera.nome}"/></h4>
                            <h6>Posseduto da ${opera.possessore.username}</h6>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

    <script>let idArtista = "${artista.id}"</script>

<%@include file="../static/fragments/footer.jsp" %>