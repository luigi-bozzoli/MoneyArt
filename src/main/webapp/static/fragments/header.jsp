<nav class="navbar navbar-expand-xl navbar-light bg-light sticky-top" style="background-color: white !important;">
    <a class="navbar-brand" href="<c:out value="${pageContext.servletContext.contextPath}"/>/pages/home.jsp">
        <img src="<c:out value="${pageContext.servletContext.contextPath}"/>/static/image/logo-moneyart.png" alt="MoneyArt" srcset="">
    </a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="#">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Aste</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Artisti</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Marketplace</a>
            </li>
        </ul>
        <form class="searchbar form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="Cerca ..." aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><i class="fas fa-search"></i></button>
        </form>
        <c:choose>
            <c:when test="${empty sessionScope.utente}">
                <div class="log-buttons">
                    <a href="<c:out value="${pageContext.servletContext.contextPath}"/>/pages/login.jsp" class="login-button">Login</a>
                    <a href="<c:out value="${pageContext.servletContext.contextPath}"/>/pages/signup.jsp" class="signup-button">Signup</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="account-buttons">
                    <a href="<c:out value="${pageContext.servletContext.contextPath}"/>/pages/profiloUtente.jsp" title="Account">
                        <i class="fas fa-user-alt"></i>
                    </a>

                    <a href="<c:out value="${pageContext.servletContext.contextPath}"/>/logout" title="Esci">
                        <i class="fas fa-sign-out-alt"></i>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</nav>