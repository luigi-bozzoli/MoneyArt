<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - MoneyArt</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/static/style/homepage_style.css">
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

    <!-- HERO SECTION -->
    <div class="row hero">
        <div class="col left-hero">
            <h1>L'arte che diventa<br>
                <span class="inverted-capital">N</span>on
                <span class="inverted-capital">F</span>ungible
                <span class="inverted-capital">T</span>oken</h1>
            <div class="hero-buttons">
                <a href="#" class="button">Esplora</a>
                <a href="#" class="button inverted">Crea</a>
            </div>
        </div>

        <div class="col right-hero" style="padding-right: 30px">
            <img class="hero-image" src="<%=request.getContextPath()%>/static/image/the-last-supper.jpg" width="800px" alt="The Last Supper" style="border-radius: 50px;">
        </div>
    </div>

    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="#fff8d9" fill-opacity="1" d="M0,64L60,74.7C120,85,240,107,360,122.7C480,139,600,149,720,133.3C840,117,960,75,1080,74.7C1200,75,1320,117,1380,138.7L1440,160L1440,0L1380,0C1320,0,1200,0,1080,0C960,0,840,0,720,0C600,0,480,0,360,0C240,0,120,0,60,0L0,0Z" data-darkreader-inline-fill="" style="--darkreader-inline-fill:#007acc;"></path></svg>
</body>

</html>