<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/header.jsp" %>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>


<jsp:include page="/serchUsers?name=${param.action}"/>
<c:set var="utenti" value="${requestScope.utentiCercati}"/>


<div class="page-heading">
    <h1>Utenti</h1>
    <span class="line-break"></span>
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
                            <a href="${pageContext.servletContext.contextPath}/getUser?id=${utente.id}" class="btn btn-primary">Visualizza profilo</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

    <!-- /ARTISTI TAB-->

    <div class="modal"><!-- Place at bottom of page --></div>






<%@include file="../static/fragments/footer.jsp" %>

