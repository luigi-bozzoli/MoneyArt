<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

<%@include file="../static/fragments/sidebar.jsp" %>
<jsp:include page="/notifies"/>
<c:set var="notifiche" value="${requestScope.notifiche}"/>
<script>let ctx = "${pageContext.servletContext.contextPath}"</script>

<div class="page-heading">
    <h1>Notifiche</h1>
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
                    <c:forEach var="notifica" items="${notifiche}">
                        <c:if test="${not notifica.isLetta()}">
                            <c:choose>
                                <c:when test="${not empty notifica.asta.opera}">
                                    <tr class="">
                                    <c:choose>
                                        <c:when test="${notifica.tipo.toString() eq 'SUPERATO'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">Sei stato superato nell'asta dell'opera ${notifica.asta.opera.nome}</td>

                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'TERMINATA'}">

                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">l'asta dell'opera ${notifica.asta.opera.nome} ?? terminata</td>


                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'ANNULLAMENTO'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">l'asta dell'opera ${notifica.asta.opera.nome} ?? stata annullata</td>

                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'VITTORIA'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">Hai vinto l'asta dell'opera ${notifica.asta.opera.nome}</td>

                                        </c:when>
                                    </c:choose>
                                </c:when>
                                <c:when test="${not empty notifica.rivendita.opera}">
                                    <tr class="">
                                    <c:choose>
                                        <c:when test="${notifica.tipo.toString() eq 'TERMINATA'}">

                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getResell?id=${notifica.rivendita.id}'">Hanno acquistato la tua opera ${notifica.rivendita.opera.nome}</td>
                                        </c:when>
                                    </c:choose>
                                </c:when>
                            </c:choose>


                            <td class="d-flex justify-content-end td"><button value="${notifica.id}"  href="#" class="btn leggi">segna come letta</button></td>

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
                    <c:forEach var="notifica" items="${notifiche}">
                        <c:if test="${notifica.isLetta()}">
                            <c:choose>
                                <c:when test="${not empty notifica.asta.opera}">
                                    <tr class="">
                                    <c:choose>
                                        <c:when test="${notifica.tipo.toString() eq 'SUPERATO'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">Sei stato superato nell'asta dell'opera ${notifica.asta.opera.nome}</td>

                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'TERMINATA'}">

                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">l'asta dell'opera ${notifica.asta.opera.nome} ?? terminata</td>


                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'ANNULLAMENTO'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">l'asta dell'opera ${notifica.asta.opera.nome} ?? stata annullata</td>

                                        </c:when>
                                        <c:when test="${notifica.tipo.toString() eq 'VITTORIA'}">
                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getAuction?id=${notifica.asta.id}'">Hai vinto l'asta dell'opera ${notifica.asta.opera.nome}</td>

                                        </c:when>
                                    </c:choose>
                                </c:when>
                                <c:when test="${not empty notifica.rivendita.opera}">
                                    <tr class="">
                                    <c:choose>
                                        <c:when test="${notifica.tipo.toString() eq 'TERMINATA'}">

                                            <td onclick="window.location.href = '${pageContext.servletContext.contextPath}/getResell?id=${notifica.rivendita.id}'">Hanno acquistato la tua opera ${notifica.rivendita.opera.nome}</td>
                                        </c:when>
                                    </c:choose>
                                </c:when>
                            </c:choose>


                            <td class="d-flex justify-content-end td"><button value="${notifica.id}"  href="#" class="btn non-leggi">segna come non letta</button><button value="${notifica.id}"  href="#" class="btn elimina ml-3">elimina</button></td>

                            </tr>

                        </c:if>
                    </c:forEach>

                    </tbody>
                </table>
            </div>


        </div>
        <!-- /Lette TAB-->

        <div class="modal"><!-- Place at bottom of page --></div>
    </div>

</div>

<%@include file="../static/fragments/footer.jsp" %>


