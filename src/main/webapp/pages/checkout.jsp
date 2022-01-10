<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
