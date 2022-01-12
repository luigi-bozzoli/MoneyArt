<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../static/fragments/sidebar.jsp"%>

    <div class="content-wrapper container mt-4 mb-4 p-3 d-flex justify-content-center">
        <div class="card p-4">
            <div class=" image d-flex flex-column justify-content-center align-items-center">

                <button class="btn-profile btn-secondary">
                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/user-placeholder.png" alt="Foto Profilo" height="100" width="100" />
                </button>

                <span class="name mt-3">NOME COGNOME</span>
                <span class="idd">@USERNAME</span>

                <div class="d-flex flex-row justify-content-center align-items-center gap-2">
                    <span class="idd1">user@email.it <i class="fas fa-envelope"></i></span>
                </div>

                <div class="d-flex flex-row justify-content-center align-items-center mt-3">
                    <span class="number">
                        1000 <span class="follow">Followers</span>
                    </span>
                </div>
                <div class=" d-flex mt-2">
                    <button class="btn1 edit-profile">Modifica Profilo</button>
                </div>
            </div>
        </div>
    </div>

<%@include file="../static/fragments/footer.jsp"%>

