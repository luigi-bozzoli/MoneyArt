<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../../static/fragments/sidebar_admin.jsp"%>

    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>

    <c:if test="${empty requestScope.admin}" >
        <c:redirect url="/adminPage"/>
    </c:if>
    <c:set var="user" value="${requestScope.admin}"/>

    <div class="content-wrapper">
        <div class="row center-block" style="margin-top: 50px;">
            <div class="col-lg-3 col-6 mybox-info">
                <!-- small box -->
                <div class="small-box bg-warning">
                    <div class="inner">
                        <h3>${requestScope.utentiRegistrati}</h3>
                        <p>Utenti Registrati</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-users"></i>
                    </div>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-3 col-6 mybox-info">
                <!-- small box -->
                <div class="small-box bg-success">
                    <div class="inner">
                        <h3>${requestScope.asteInCorso}</h3>

                        <p>Aste in Corso</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-chart-line"></i>
                    </div>
                </div>
            </div>
            <!-- ./col -->
            <div class="col-lg-3 col-6 mybox-info">
                <!-- small box -->
                <div class="small-box bg-info">
                    <div class="inner">
                        <h3>${requestScope.rivendite}</h3>
                        <p>Rivendite</p>
                    </div>
                    <div class="icon">
                        <i class="fas fa-shopping-cart"></i>
                    </div>
                </div>
            </div>
        </div>

        <div class="container d-flex flex-column mt-3 mb-3 profile-box align-items-center">
            <div class="propic d-flex justify-content-center mt-3">
                <img src="${pageContext.servletContext.contextPath}/userPicture?id=${user.id}" alt="Foto profilo">
                <div class="overlay d-none">
                    <div class="text"><i class="fas fa-pen"></i></div>
                </div>
            </div>
            <div class="info">
                <h2 class="text-center mt-3"><c:out value="${user.nome}"/> <c:out value="${user.cognome}"/></h2>
                <h6 class="text-center mt-3">@<c:out value="${user.username}"/></h6>
                <div class="email-info d-flex justify-content-center align-items-center">
                    <i class="fas fa-envelope"></i> <h6 class="mb-0 ml-1">${user.email}</h6>
                </div>
            </div>
        </div>
    </div>


<%@include file="../../static/fragments/footer.jsp"%>

