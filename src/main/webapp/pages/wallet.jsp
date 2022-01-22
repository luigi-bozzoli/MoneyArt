<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../static/fragments/sidebar.jsp"%>

<script>let ctx = "${pageContext.servletContext.contextPath}"</script>

<c:if test="${empty requestScope.utente}" >
    <c:redirect url="/walletInfo"/>
</c:if>

<c:set var="utente" value="${requestScope.utente}"/>


<div class="content-wrapper">
    <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="ricarica-tab" data-toggle="tab" href="#ricarica" role="tab" aria-controls="auctions" aria-selected="true">Ricarica</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="prelievo-tab" data-toggle="tab" href="#prelievo" role="tab" aria-controls="artists" aria-selected="true">Prelievo</a>
        </li>
    </ul>
    <!-- /Nav Tab -->


    <div class="tab-content" id="myTabContent">
        <!-- RICARICA TAB-->

        <div class="tab-pane fade show active" id="ricarica" role="tabpanel" aria-labelledby="ricarica-tab">
            <div class="container">
                <div class="row m-0">
                    <div class="col-md-7 col-12">
                        <div class="row">
                            <div class="col-12 mb-4">
                                <div class="row box-right">
                                    <div class="col-md-8 ps-0 ">
                                        <p class="ps-3 textmuted fw-bold h6 mb-0">SALDO</p>
                                        <p class="h1 fw-bold d-flex"> <i class="fas fa-euro-sign textmuted pe-1 h6 align-text-top mt-1"></i><c:out value="${utente.saldo}"/>

                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 px-0 mb-4">
                                <div class="box-right">
                                    <div class="d-flex pb-2">
                                        <p class="fw-bold h7">Come Pagare?</p>
                                    </div>
                                    <div class="bg-blue p-2">
                                        <P class="h8 textmuted">Puoi pagare e prelevare comodamente scegliendo fra i nostri 2 metodi di pagamento sicuri: PayPal o Carta.
                                        <p class="p-blue bg btn btn-primary h8">LEARN MORE</p>
                                        </P>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 px-0">
                                <div class="box-right">
                                    <div class="d-flex mb-2">
                                        <p class="fw-bold">Info</p>
                                    </div>
                                    <div class="row">
                                        <div class="col-12 mb-2">
                                            <p>Ricarica il tuo saldo MoneyArt usando una carta di credito o di debito e/o PayPal: potrai acquistare milioni di articoli su MoneyArt comodamente da casa tua.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-5 col-12 ps-md-5 p-0 ">
                        <div class="box-left">
                            <p class="textmuted h8">Account</p>
                            <p class="fw-bold h7"><c:out value="${utente.nome}"/> <c:out value="${utente.cognome}"/></p>
                            <p class="textmuted h8">@<c:out value="${utente.username}"/></p>
                            <p class="textmuted h8 mb-2"><c:out value="${utente.email}"/></p>
                            <div>
                                <p class="h7 fw-bold mb-1">Prosegui al pagamento:</p>
                                <p class="textmuted h8 mb-2">Procedi al pagamento tramite le nostre piattaforme proposte</p>
                                <form action="wallet" method="get">
                                    <input type="hidden" name="action" value="deposit">
                                    <div class="form">
                                        <div class="row">
                                            <div class="col-12">
                                                <div> <input name="amount" class="form-control" type="number" required="true" placeholder="100.00"></div>
                                            </div>
                                            <p class="p-blue h8 fw-bold mb-3">senza commissioni aggiuntive</p>
                                        </div>
                                        <button type="submit" name="method" value="stripe" class="btn btn-primary btn-carta d-block h8"> <i class="fab fa-cc-mastercard"></i> <i class="fab fa-cc-visa"></i> Procedi con Carta<span class="ms-3 fas fa-arrow-right"></span></button>
                                        <br>
                                        <button type="submit" name="method" value="PayPal" class="btn btn-primary d-block h8"> <i class="fab fa-paypal"></i> Procedi con PayPal<span class="ms-3 fas fa-arrow-right"></span></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /RICARICA TAB-->
        </div>


        <!-- PRELIEVO TAB-->
        <div class="tab-pane fade" id="prelievo" role="tabpanel" aria-labelledby="prelievo-tab">
            <div class="container">
                <div class="row m-0">
                    <div class="col-md-7 col-12">
                        <div class="row">
                            <div class="col-12 mb-4">
                                <div class="row box-right">
                                    <div class="col-md-8 ps-0 ">
                                        <p class="ps-3 textmuted fw-bold h6 mb-0">SALDO</p>
                                        <p class="h1 fw-bold d-flex"> <i class="fas fa-euro-sign textmuted pe-1 h6 align-text-top mt-1"></i><c:out value="${utente.saldo}"/>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 px-0 mb-4">
                                <div class="box-right">
                                    <div class="d-flex pb-2">
                                        <p class="fw-bold h7">Come Prelevare?</p>
                                    </div>
                                    <div class="bg-blue p-2">
                                        <P class="h8 textmuted">Puoi prelevare comodamente fornendo il tuo IBAN. Riceverai l'importo in un massimo di 5 giorni.
                                        <p class="p-blue bg btn btn-primary h8">LEARN MORE</p>
                                        </P>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12 px-0">
                                <div class="box-right">
                                    <div class="d-flex mb-2">
                                        <p class="fw-bold">Info</p>
                                    </div>
                                    <div class="row">
                                        <div class="col-12 mb-2">
                                            <p>Preleva il tuo saldo MoneyArt usando un conto bancario a te intestato: basterà inserire il tuo iban e procedere all'operazione!</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-5 col-12 ps-md-5 p-0 ">
                        <div class="box-left">
                            <p class="textmuted h8">Account</p>
                            <p class="fw-bold h7"><c:out value="${utente.nome}"/> <c:out value="${utente.cognome}"/></p>
                            <p class="textmuted h8">@<c:out value="${utente.username}"/></p>
                            <p class="textmuted h8 mb-2"><c:out value="${utente.email}"/></p>
                            <div>
                                <p class="h7 fw-bold mb-1">Prosegui al pagamento:</p>
                                <p class="textmuted h8 mb-2">Procedi al pagamento tramite le nostre piattaforme proposte</p>
                                <form action="wallet" method="get">
                                    <div class="form">
                                        <input type="hidden" name="action" value="withdraw">
                                        <div class="row">
                                            <div class="col-12">
                                                <div> <input class="form-control" type="number" name="amount" required="true" placeholder="€100.00"></div>
                                            </div>
                                            <p class="p-blue h8 fw-bold mb-3">commissione di 1€</p>
                                            <div class="col-12">
                                                <div> <input class="form-control" type="text" name="iban" required="true" placeholder="ITSCDJFEGIUFEOJIOEC"></div>
                                            </div>
                                        </div>
                                        <br>
                                        <button type="submit" class="btn btn-primary btn-carta d-block h8">Preleva Importo<span class="ms-3 fas fa-arrow-right"></span></button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /PRELIEVO TAB-->
    </div>
<%@include file="../static/fragments/footer.jsp"%>