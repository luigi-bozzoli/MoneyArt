
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registrazione - MoneyArt</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/style/signup_style.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/style/fragments_style.css">

    <!-- FAVICON -->
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="<%=request.getContextPath()%>/static/favicon.ico" type="image/x-icon">

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

    <div class="page-heading">
        <h1>Crea un nuovo account</h1>
        <span class="line-break"></span>
    </div>

    <div class="signup-box">
        <img class="signup-logo" src="<%=request.getContextPath()%>/static/image/logo-moneyart.png" alt="Logo MoneyArt">
        <form class="signup" method="post" name="signup">
            <div class="signup-input">
                <label for="name">Nome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="name" id="name" type="text" placeholder="John">
                </div>
            </div>

            <div class="signup-input">
                <label for="surname">Cognome</label>
                <div class="input-icon">
                    <i class="fas fa-user-tie"></i>
                    <input name="surname" id="surname" type="text" placeholder="Doe">
                </div>
            </div>

            <div class="signup-input">
                <label for="email">Email</label>
                <div class="input-icon">
                    <i class="fas fa-envelope"></i>
                    <input name="email" id="email" type="email" placeholder="yourmail@mail.com">
                </div>
            </div>

            <div class="signup-input">
                <label for="username">Username</label>
                <div class="input-icon">
                    <i class="fas fa-at"></i>
                    <input name="username" id="username" type="text" placeholder="JohnDoe99">
                </div>
            </div>

            <div class="signup-input">
                <label for="password">Password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="password" id="password" type="password" placeholder="Password">
                </div>
            </div>

            <div class="signup-input">
                <label for="repeat-password">Ripeti password</label>
                <div class="input-icon">
                    <i class="fas fa-key"></i>
                    <input name="repeat-password" id="repeat-password" type="password" placeholder="Repeat password">
                </div>
            </div>

            <button type="submit">Registrati</button>
        </form>
    </div>



</body>
</html>
