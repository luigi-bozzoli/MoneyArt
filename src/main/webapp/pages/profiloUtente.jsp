<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="../static/fragments/sidebar.jsp"%>

    <c:if test="${empty requestScope.utente}" >
        <c:redirect url="/userPage"/>
    </c:if>
    <c:set var="user" value="${requestScope.utente}"/>


    <div class="container d-flex flex-column mt-3 mb-3 profile-box">
        <div class="propic d-flex justify-content-center mt-3">
            <img src="${pageContext.servletContext.contextPath}/userPicture?id=${user.id}" alt="Foto profilo">
        </div>
        <div class="info">
            <h2 class="text-center mt-3"><c:out value="${user.nome}"/> <c:out value="${user.cognome}"/></h2>
            <h6 class="text-center mt-3">@<c:out value="${user.username}"/></h6>
            <div class="email-info d-flex justify-content-center align-items-center">
                <i class="fas fa-envelope"></i> <h6 class="mb-0 ml-1">${user.email}</h6>
            </div>
        </div>

            <div class="balance-info d-flex justify-content-center align-items-baseline mt-3">
                <h4 class="mb-0">Saldo:</h4>
                <h5 class="mb-0 ml-2"><fmt:formatNumber type="currency" value="${user.saldo}"/></h5>
            </div>

            <div class="followers-info d-flex justify-content-center align-items-baseline mt-2">
                <h4>Followers:</h4>
                <h5 class="ml-2"><c:out value="${user.nFollowers}"/></h5>
            </div>


            <div class="following d-flex justify-content-center align-items-baseline mb-3">
                <h4 class="mb-0">Artista seguito:</h4>

                <c:choose>
                    <c:when test="${empty user.seguito.id}">
                        <h5 class="following-username mb-0 ml-2">Nessuno</h5>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a class="following-username ml-2" href="#"><c:out value="${user.seguito.username}"/></a>
                        </div>
                        <div class="container d-flex justify-content-center">
                            <a class="unfollow text-center" href="#">Smetti di seguire</a>
                        </div>
                    </c:otherwise>
                </c:choose>



    <div class="update-informations d-none">
        <!-- Form -->
        <form class="data" method="post" name="informations" action="">
            <div class="data-input">
                <label for="name">Nome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="name" id="name" type="text" placeholder="John" value="${user.nome}" required>
                </div>
            </div>

            <div class="data-input">
                <label for="surname">Cognome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="surname" id="surname" type="text" placeholder="Doe" value="${user.cognome}" required>
                </div>
            </div>

            <div class="data-input">
                <label for="email">Email</label>
                <div class="input-icon">
                    <i class="fas fa-envelope"></i>
                    <input name="email" id="email" type="text" placeholder="yourmail@mail.com" value="${user.email}" required>
                </div>
            </div>

            <div class="data-input">
                <label for="username">Username</label>
                <div class="input-icon">
                    <i class="fas fa-at"></i>
                    <input name="username" id="username" type="text" placeholder="JohnDoe99" value="${user.username}" requirede>
                </div>
            </div>

            <div class="data-input pwc">
                <label for="confirm-password">Conferma password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="confirm-password" id="confirm-password" type="password" placeholder="Dimostra che sei ${user.nome}">
                </div>
            </div>

            <div class="data-input pw d-none">
                <label for="password">Password attuale</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="password" id="password" type="password" placeholder="Password attuale">
                </div>
            </div>

            <div class="data-input pw d-none">
                <label for="new-password">Nuova password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="new-password" id="new-password" type="password" placeholder="Nuova password">
                </div>
            </div>


            <button id="update-password" class="button text-center ml-3" href="#">Cambia password</button>

        </form>
        <!-- /Form -->
    </div>

</div>

<button id="update-profile" class="button text-center" href="#">Modifica profilo</button>

<div id="update-choice" class="container d-none justify-content-center">
    <button id="cancel-update" class="button text-center mr-3" href="#">Annulla</button>
    <button id="confirm-update" class="button text-center" href="#">Conferma Modifica profilo</button>
</div>

    <script>
        $(document).ready(function () {
            $('#update-profile').click(function () {
                $('.info, .balance-info, .followers-info, .following, .unfollow').removeClass('d-flex').addClass('d-none');
                $('.update-informations').removeClass('d-none');
                $('#update-choice').removeClass('d-none').addClass('d-flex');
                $('#update-profile').addClass('d-none');
            });
            $('#cancel-update').click(function () {
                $('.info').removeClass('d-none')    ;
                $('.balance-info, .followers-info, .following, .unfollow').removeClass('d-none').addClass('d-flex');
                $('.update-informations').addClass('d-none');
                $('#update-choice').removeClass('d-flex').addClass('d-none');
                $('#update-profile').removeClass('d-none');
                $('#password, #new-password, #confirm-password').val('');

                if(!$('.pw').hasClass('d-none')) {
                    $('.pw').addClass('d-none');
                    $('.pwc').removeClass('d-none');
                }
                $('#update-password').removeClass('d-none');
            });
            $('#update-password').click(function (event) {
                $('.pw').removeClass('d-none');
                $('.pwc').addClass('d-none');
                $('#update-password').addClass('d-none');
                event.preventDefault();
            });

            if($('.pw').hasClass('d-none')) {
                $('#confirm-password').keyup(function () {
                    let value = $('#confirm-password').val();
                    if(value.length > 2) {
                        $.post("${pageContext.servletContext.contextPath}/changeUserInformation", $.param({'confirmPassword' : value}), function (response) {
                            if(response) {
                                $(".data-input.pwc").css("border", "none");
                                $("#confirm-update").css("pointer-events", "auto");
                            } else {
                                $(".data-input.pwc").css("border", "1px solid red");
                                $("#confirm-update").css("pointer-events", "none");
                            }


                        });
                    }
                });
            }
        });
    </script>


<%@include file="../static/fragments/footer.jsp"%>

