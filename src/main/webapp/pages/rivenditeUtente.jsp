<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>


<jsp:include page="/getResells?action="/>
<c:set var="rivendite" value="${requestScope.rivendite}"/>


<div class="page-heading">
    <h1>Le tue rivendite</h1>
    <span class="line-break"></span>
</div>

<!-- /Nav Tab -->

        <div class="container-fluid d-flex flex-wrap" id="container-aste">

            <c:forEach var="rivendita" items="${rivendite}">
                <c:if test="${(rivendita.opera.possessore.id == sessionScope.utente.id) && (rivendita.stato eq 'IN_CORSO')}">
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

                            <a href="${pageContext.servletContext.contextPath}/getResell?id=${rivendita.id}" class="btn btn-primary">Vai alla rivendita</a>
                        </div>
                    </div>
                </div>
                </c:if>
            </c:forEach>
        </div>



    <div class="modal"><!-- Place at bottom of page --></div>







<%@include file="../static/fragments/footer.jsp" %>


