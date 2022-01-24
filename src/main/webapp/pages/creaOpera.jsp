<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../static/fragments/sidebar.jsp" %>
    <script>let ctx = "${pageContext.servletContext.contextPath}"</script>

    <c:set var="utente" value="${sessionScope.utente}"/>

    <div class="page-heading">
        <h1>Crea una nuova opera</h1>
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
    <div class="container d-flex flex-column mt-3 mb-3 align-items-center artwork-box">
        <div class="image-wrapper">
            <h2 class="text-center mb-3">Immagine</h2>
            <div class="text-center d-none image-crop-wrap" style="padding-top:30px;">
                <div id="upload-input"></div>
                <div class="icons d-flex justify-content-center">
                    <i class="fas fa-times mr-3" id="cancel-crop"></i>
                    <i class="fas fa-check ml-3" id="crop-result"></i>
                </div>

            </div>
            <div class="pic d-flex justify-content-center mt-3 ml-auto mr-auto">
                <img src="${pageContext.servletContext.contextPath}/static/image/noimage.jpg" alt="Foto Opera">
                <div class="overlay" onclick="picClick()">
                    <div class="text"><i class="fas fa-pen"></i></div>
                </div>
            </div>
        </div>

        <div class="create-artwork">
            <form class="data" method="post" name="informations" action="${pageContext.servletContext.contextPath}/newArtwork" enctype='multipart/form-data'>
                <div class="data-input">
                    <label for="name">Nome opera</label>
                    <div class="input-icon">
                        <i class="fas fa-paint-brush"></i>
                        <input name="name" id="name" type="text" placeholder="Nome dell'opera">
                    </div>
                </div>

                <div class="data-input">
                    <label for="description">Descrizione</label>
                    <div class="input-icon">
                        <textarea name="description" id="description" placeholder="Inserisci una descrizione dell'opera" rows="5" style="resize: none;"></textarea>
                    </div>
                </div>

                <input name="picture" id="picForm" type="hidden" required>

                <button id="create" class="button text-center" href="#">Crea opera</button>
            </form>

        </div>

        <input name="pictureCr"  id="picCropped" type="file" class="d-none" accept="image/*">
    </div>

<%@include file="../static/fragments/footer.jsp" %>