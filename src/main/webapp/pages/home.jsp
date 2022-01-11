<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@include file="../static/fragments/header.jsp"%>
    <c:set var="aste" value="${requestScope.aste}"/>

    <c:if test="${empty aste}">
        <c:redirect url = "/getAuctions?action=inCorso&page=home"/>
    </c:if>
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
            <img class="hero-image" src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/the-last-supper.jpg" width="800px" alt="The Last Supper" style="border-radius: 50px;">
        </div>
    </div>

    <img class="wave" src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/wave.svg">

    <div class="container auction-carousel large" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-xl" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-xl" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-xl" data-slide-to="1"></li>
            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <div class="col-md-4">
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
                        <div class="col-md-4">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/shibosis.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Shibosis</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/tiger.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>TIGXR</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <div class="col-md-4">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/capsule-house.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Capsule House</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/kumo-resident.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Kumo Resident</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/miner.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Miner</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Second Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-xl" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-xl" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>

    <div class="container auction-carousel medium" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-md" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-md" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-md" data-slide-to="1"></li>
                <li data-target="#carousel-md" data-slide-to="2"></li>
            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <div class="col-md-6">
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
                        <div class="col-md-6">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/shibosis.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Shibosis</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <div class="col-md-6">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/capsule-house.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Capsule House</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/kumo-resident.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Kumo Resident</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Second Slide-->
                    <!-- Third Slide-->
                    <div class="carousel-item">
                        <div class="col-md-6">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/tiger.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>TIGXR</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/miner.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Miner</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Third Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-md" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-md" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>

    <div class="container auction-carousel small" style="margin-bottom: 10vh">
        <div class="page-heading">
            <h2>Aste in corso</h2>
            <span class="line-break"></span>
        </div>
        <!-- Carousel Wrapper -->
        <div id="carousel-sd" class="carousel slide" data-ride="carousel" data-interval="7000">
            <!--Indicators-->
            <ol class="carousel-indicators">
                <li data-target="#carousel-sd" data-slide-to="0" class="active"></li>
                <li data-target="#carousel-sd" data-slide-to="1"></li>
                <li data-target="#carousel-sd" data-slide-to="2"></li>
                <li data-target="#carousel-sd" data-slide-to="3"></li>
                <li data-target="#carousel-sd" data-slide-to="4"></li>
                <li data-target="#carousel-sd" data-slide-to="5"></li>

            </ol>
            <!--/.Indicators-->
            <div class="row">
                <!-- Slides -->
                <div class="carousel-inner" role="listbox">
                    <!-- First Slide-->
                    <div class="carousel-item active">
                        <div class="col-sm-12">
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
                    <!-- /First Slide-->
                    <!-- Second Slide-->
                    <div class="carousel-item">
                        <div class="col-sm-12">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/capsule-house.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Capsule House</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Second Slide-->
                    <!-- Third Slide-->
                    <div class="carousel-item">
                        <div class="col-sm-12">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/shibosis.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Shibosis</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Third Slide-->
                    <!-- Fourth Slide-->
                    <div class="carousel-item">
                        <div class="col-sm-12">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/tiger.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>TIGXR</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Fourth Slide-->
                    <!-- Fifth Slide-->
                    <div class="carousel-item">
                        <div class="col-sm-12">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/kumo-resident.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>Kumo Resident</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Sixth Slide-->
                    <div class="carousel-item">
                        <div class="col-sm-12">
                            <div class="thumb-wrapper">
                                <div class="img-box">
                                    <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/demo/miner.jpg" class="img-responsive">
                                </div>
                                <div class="thumb-content">
                                    <h4>The Miner</h4>
                                    <div class="expiration-timer">
                                        <span class="timer">12:29:59</span>
                                    </div>
                                    <p class="item-price">€3024,33</p>
                                    <a href="#" class="btn btn-primary">Vai all'asta</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /Sixth Slide-->
                </div>
                <!-- /Slides -->
            </div>
            <!-- Controls-->
            <a class="carousel-control-prev" href="#carousel-sd" role="button" data-slide="prev">
                <i class="fas fa-chevron-left"></i>
                <span class="sr-only">Previous</span>
            </a>
            <a class="carousel-control-next" href="#carousel-sd" role="button" data-slide="next">
                <i class="fas fa-chevron-right"></i>
                <span class="sr-only">Next</span>
            </a>
            <!-- Controls-->
        </div>
        <!-- /Carousel Wrapper -->
    </div>


    <ul>
        <c:forEach items="${aste}" var="asta">
            <li>
                <c:out value="${asta.opera.nome}"/>
            </li>
        </c:forEach>

    </ul>
<%@include file="../static/fragments/footer.jsp"%>
