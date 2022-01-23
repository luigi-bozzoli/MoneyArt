<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>



    <jsp:include page="/getResells?action=inCorso"/>
    <c:set var="rivendite" value="${requestScope.rivendite}"/>

    <div class="page-heading">
        <h1>Marketplace</h1>
        <span class="line-break"></span>
    </div>

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
            </div>
        </div>
    </div>

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

    <div class="modal"><!-- Place at bottom of page --></div>

<%@include file="../static/fragments/footer.jsp" %>