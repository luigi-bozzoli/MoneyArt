<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../static/fragments/header.jsp"%>

        <!-- Nav Tab -->
        <ul class="nav nav-tabs d-flex justify-content-between" role="tablist">
                <li class="nav-item">
                        <a class="nav-link active" id="aste-tab" data-toggle="tab" href="#auctions" role="tab" aria-controls="auctions" aria-selected="true">Aste</a>
                </li>
                <li class="nav-item">
                        <a class="nav-link" id="artisti-tab" data-toggle="tab" href="#artists" role="tab" aria-controls="artists" aria-selected="true">Artisti</a>
                </li>
        </ul>
        <!-- /Nav Tab -->

        <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="auctions" role="tabpanel" aria-labelledby="aste-tab">
                        <div class="container-fluid d-flex flex-wrap ">
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>
                                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                                        <div class="thumb-wrapper">
                                                <div class="img-box">
                                                        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/cupcat.jpg" class="img-responsive">
                                                </div>
                                                <div class="thumb-content">
                                                        <h4>CupCat</h4>
                                                        <div class="expiration-timer">
                                                                <span class="timer">12:29:59</span>
                                                        </div>
                                                        <p class="item-price">€3024,33</p>
                                                        <a href="#" class="btn btn-primary">Vai all'asta</a>
                                                </div>
                                        </div>
                                </div>

                        </div>
                </div>
                <div class="tab-pane fade" id="artists" role="tabpanel" aria-labelledby="artisti-tab">
                        ARTISTIIIII
                </div>

        </div>

<%@include file="../static/fragments/footer.jsp"%>