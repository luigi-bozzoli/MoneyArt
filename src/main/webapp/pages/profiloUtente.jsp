<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Profilo Utente - MoneyArt</title>

        <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/homepage_style.css">
        <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/fragments_style.css">
        <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/template_bootstrap.css">
        <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/profiloUtente_style.css">


        <!-- FAVICON -->
        <link rel="shortcut icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">
        <link rel="icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">

        <!-- BOOTSTRAP CDN -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

        <!-- FONTAWESOME CDN -->
        <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />
    </head>

    <body>
        <%@include file="../static/fragments/header.jsp"%>

            <div class="container mt-4 mb-4 p-3 d-flex justify-content-center">
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
    </body>

    </html>