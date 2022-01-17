<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <%@include file="../static/fragments/header.jsp"%>

            <body>
                <div class="container d-md-flex align-items-center">
                    <div class="card box1 shadow-sm p-md-5 p-md-5 p-4">
                        <div class="fw-bolder mb-4">
                            </span><span class="ps-1">€1000.00</span></div>
                        <div class="d-flex flex-column">
                            <div class="d-flex align-items-center justify-content-between text">
                                </span>
                            </div>
                            <div class="d-flex align-items-center justify-content-between text mb-4"> <span>Totale</span><span class="ps-1">€1000</span></span>
                            </div>
                            <div class="border-bottom mb-4"></div>
                            <div class="d-flex flex-column mb-5"> <span class="far fa-calendar-alt text"><span class="ps-2">Data Pagamento:</span></span> <span class="ps-3">27-01-2001</span> </div>
                        </div>
                    </div>
                    <div class="card box2 shadow-sm">
                        <div class="d-flex align-items-center justify-content-between p-md-5 p-4"> <span class="h5 fw-bold m-0">Metodo di Pagamento</span>
                        </div>
                        <ul class="nav nav-tabs mb-3 px-md-4 px-2">
                            <li class="nav-item"> <a class="nav-link px-2 active" aria-current="page" href="#">Carta</a> </li>
                        </ul>
                        <form action="">
                            <div class="row">
                                <div class="col-12">
                                    <div class="d-flex flex-column px-md-5 px-4 mb-4"> <span>N. Carta di Credito/Debito</span>
                                        <div class="inputWithIcon"> <input class="form-control" type="text" value="5136 1845 5468 3894"> </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="d-flex flex-column ps-md-5 px-md-0 px-4 mb-4"> <span>Scadenza<span class="ps-1">Data</span></span>
                                        <div class="inputWithIcon"> <input type="text" class="form-control" value="05/20"> <span class="fas fa-calendar-alt"></span> </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="d-flex flex-column pe-md-5 px-md-0 px-4 mb-4"> <span>CVV</span>
                                        <div class="inputWithIcon"> <input type="password" class="form-control" value="123"> <span class="fas fa-lock"></span> </div>
                                    </div>
                                </div>
                                <div class="col-12">
                                    <div class="d-flex flex-column px-md-5 px-4 mb-4"> <span>Nome e Cognome</span>
                                        <div class="inputWithIcon"> <input class="form-control text-uppercase" type="text" value="valdimir berezovkiy"> <span class="far fa-user"></span> </div>
                                    </div>
                                </div>
                                <div class="col-12 px-md-5 px-4 mt-3">
                                    <div class="btn btn-primary w-100">Paga €1000.00</div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </body>
            <%@include file="../static/fragments/footer.jsp"%>