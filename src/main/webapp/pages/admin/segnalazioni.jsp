<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@include file="../../static/fragments/sidebar_admin.jsp" %>

<script>let ctx = "${pageContext.servletContext.contextPath}"</script>

<c:if test="${empty requestScope.segnalazioni}" >
    <c:redirect url="/getReports"/>
</c:if>
<c:set var="segnalazioni" value="${requestScope.segnalazioni}"/>

<div class="page-heading">
    <h1>Segnalazioni</h1>
    <span class="line-break"></span>
</div>

<div class="container">
    <!-- Nav Tab -->
    <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="nuove-tab" data-toggle="tab" href="#da-leggere" role="tab" aria-controls="da-leggere"
               aria-selected="true">Nuove</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="lette-tab" data-toggle="tab" href="#lette" role="tab" aria-controls="artists"
               aria-selected="true">Lette</a>
        </li>
    </ul>
    <!-- /Nav Tab -->
    <div class="tab-content" id="myTabContent">
        <!-- Nuove TAB-->
        <div class="tab-pane fade show active" id="da-leggere" role="tabpanel" aria-labelledby="da-leggere">

            <div class="container-fluid d-flex flex-wrap" id="container-nuove">

                <table class="table table-hover non-lette " id="non-lette">
                    <tbody class="">
                    <c:forEach var="segnalazione" items="${segnalazioni}">
                        <c:if test="${not segnalazione.isLetta()}">
                            <tr>
                                <td>${segnalazione.commento}</td>
                                <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${segnalazione.asta.id}'">Vai all'asta</td>
                                <td class="d-flex justify-content-end td">
                                    <button value="${segnalazione.id}"  href="#" class="btn leggi">Letta</button>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- /Nuove TAB-->


        <!-- Lette TAB-->
        <div class="tab-pane fade" id="lette" role="tabpanel" aria-labelledby="lette">

            <div class="container-fluid d-flex flex-wrap" id="container-lette">

                <table class="table table-hover lette" id = "gia-lette">
                    <tbody>
                    <c:forEach var="segnalazione" items="${segnalazioni}">
                        <c:if test="${segnalazione.isLetta()}">
                            <tr>
                                <td>${segnalazione.commento}</td>
                                <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${segnalazione.asta.id}'">Vai all'asta</td>
                                <td class="d-flex justify-content-end td">
                                    <button value="${segnalazione.id}"  href="#" class="btn non-leggi">segna come non letta</button>
                                    <button value="${segnalazione.id}"  href="#" class="btn elimina ml-3">elimina</button>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>

            </div>


        </div>
        <!-- /Lette TAB-->

    </div>

</div>


<%@include file="../../static/fragments/footer.jsp"%>
