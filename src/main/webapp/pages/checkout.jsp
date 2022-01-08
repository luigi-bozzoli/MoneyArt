<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="it">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - MoneyArt</title>

    <!-- PAGE CSS -->
    <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/fragments_style.css">
    <link rel="stylesheet" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/style/checkout_style.css">

    <!-- FAVICON -->
    <link rel="shortcut icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">
    <link rel="icon" href="<c:out value="${pageContext.servletContext.contextPath}"/>/static/favicon.ico" type="image/x-icon">
    <!-- JQUERY CDN -->
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30=" crossorigin="anonymous"></script>

    <!-- BOOTSTRAP CDN -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>

    <!-- FONTAWESOME CDN -->
    <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />

</head>

<body>
    <%@include file="../static/fragments/header.jsp"%>

    <section class="container">
        <section class="content">
            <article id="checkoutNav" class="shadow">
                <ul>
                    <li class="active">
                        <p>01</p>
                        <i class="fa fa-credit-card" aria-hidden="true"></i>
                        <p>Pagamento</p>
                    </li>
                    <li>
                        <p>02</p>
                        <i class="fas fa-thumbs-up"></i>
                        <p>Successo</p>
                    </li>
                </ul>
            </article>
            <article id="product" class="shadow"><img src="https://static.open.online/wp-content/uploads/2021/12/Nft-2-1280x677.jpg" alt="nft"></article>
            <div class="title-product">
                <h1>NOME NFT</h1>
                <h5>by @artista</h5>
            </div>
            <article id="checkoutCard" class="shadow">
                <div id="details">
                    <dl class="">
                        <dt>Prodotto</dt>
                        <dd> <img id="thumbnail" src="https://static.open.online/wp-content/uploads/2021/12/Nft-2-1280x677.jpg" alt="Lunar 2"></dd>
                        <dt></dt>
                        <dd></dd>
                        <dt>Prezzo</dt>
                        <dd> $69.99 </dd>
                    </dl>
                </div>
                <form action="">
                    <div id="card-checkout">
                        <div id="cards">
                            <ul>
                                <li><label for="" name="Card Type">Riepilogo Ordine</label></li>
                                <li><i class="fa fa-cc-visa" aria-hidden="true"></i></li>
                                <li><i class="fa fa-cc-paypal" aria-hidden="true"></i></li>
                                <li><i class="fa fa-cc-amex" aria-hidden="true"></i></li>
                                <li><i class="fa fa-cc-mastercard" aria-hidden="true"></i></li>
                            </ul>
                        </div>
                        <div id="riepilogo-info">
                            <ul>
                                <li>
                                    <p>Nome NFT</p>
                                </li>
                                <li>
                                    <p>
                                        <a href="#">username Artista</a>
                                    </p>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <input type="submit" value="Check out" id="btnSubmit">
                </form>
            </article>
        </section>
    </section>

    <%@include file="../static/fragments/footer.jsp"%>
</body>

</html>