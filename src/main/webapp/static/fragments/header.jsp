<nav class="navbar navbar-expand-xl navbar-light bg-light sticky-top" style="background-color: white !important;">
    <a class="navbar-brand" href="<%=request.getContextPath()%>/pages/home.jsp">
        <img src="<%=request.getContextPath()%>/static/image/logo-moneyart.png" alt="MoneyArt" srcset="">
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
        <div class="log-buttons">
            <a href="#" class="login-button">Login</a>
            <a href="<%=request.getContextPath()%>/pages/signup.jsp" class="signup-button">Signup</a>
        </div>
    </div>
</nav>
