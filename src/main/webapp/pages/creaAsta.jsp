<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/sidebar.jsp"%>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>

    <c:set var="utente" value="${sessionScope.utente}"/>
    <c:set var="opera" value="${requestScope.opera}"/>

    <div class="page-heading">
        <h1>Creazione asta</h1>
        <span class="line-break"></span>
    </div>

    <div class="container">
        <div class="col-12">
            <div class="error">
                <c:if test="${not empty requestScope.error}">
                    <p class="text-center" style="color: #BB371A !important;">${requestScope.error}</p>
                </c:if>
            </div>
        </div>
    </div>

    <div class="container d-flex flex-column mt-3 mb-3 align-items-center auction-box">
        <div class="image-wrapper">
            <div class="pic d-flex justify-content-center mt-3 ml-auto mr-auto">
                <img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}" alt="Foto Opera">
            </div>
        </div>

        <div class="create-auction">
            <form class="data" method="post" name="informations" action="${pageContext.servletContext.contextPath}/newAuction">
                <div class="data-input">
                    <label for="inizio">Data inizio</label>
                    <div class="input-icon">
                        <i class="fas fa-calendar-day"></i>
                        <input name="inizio" id="inizio" type="date" placeholder="Data inizio dell'asta" required>
                    </div>
                </div>

                <div class="data-input">
                    <label for="fine">Descrizione</label>
                    <div class="input-icon">
                        <i class="fas fa-calendar-day"></i>
                        <input name="fine" id="fine" type="date" placeholder="Data di scadenza dell'asta" required>
                    </div>
                </div>

                <input name="id" id="id" type="hidden" value="${opera.id}">

                <button type="submit"  class="button text-center">Crea asta</button>
            </form>

        </div>
    </div>



<%@include file="../static/fragments/footer.jsp" %>