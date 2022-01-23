<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <%@include file="../static/fragments/header.jsp"%>

        <script>let ctx = "${pageContext.servletContext.contextPath}"</script>

        <c:set var="rivendita" value="${requestScope.rivendita}"/>
        <c:set var="opera" value="${requestScope.opera}"/>

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
                    <article id="product" class="shadow"><img src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}" alt="nft"></article>
                    <div class="title-product">
                        <h1><c:out value="${opera.nome}"/></h1>
                        <h5>@<c:out value="${opera.artista.username}"/></h5>
                    </div>
                    <article id="checkoutCard" class="shadow">
                        <div id="details">
                            <dl class="">
                                <dt>Prodotto</dt>
                                <dd> <img id="thumbnail" src="${pageContext.servletContext.contextPath}/artworkPicture?id=${opera.id}" alt="nft"></dd>
                                <dt></dt>
                                <dd></dd>
                                <dt>Prezzo</dt>
                                <dd> €<c:out value="${rivendita.prezzo}" /> </dd>
                            </dl>
                        </div>
                        <form action="buyArtwork" method="get">
                            <input type="hidden" name="idRivendita" value="<c:out value="${rivendita.id}" />">
                            <div id="card-checkout">
                                <div id="cards">
                                    <ul>
                                        <li><label name="Card Type">Riepilogo Ordine</label></li>
                                    </ul>
                                </div>
                                <div id="riepilogo-info">
                                    <ul>
                                        <li>
                                            <p><c:out value="${opera.nome}"/></p>
                                        </li>
                                        <li>
                                            <p>
                                                <a href="#"><c:out value="${opera.artista.username}"/></a>
                                            </p>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <input type="submit" id="btnSubmit" style="cursor: pointer;">
                        </form>
                    </article>
                </section>
            </section>
            <%@include file="../static/fragments/footer.jsp"%>