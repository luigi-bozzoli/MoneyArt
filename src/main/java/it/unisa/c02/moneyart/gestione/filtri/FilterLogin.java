package it.unisa.c02.moneyart.gestione.filtri;

import javax.servlet.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "FilterLogin", urlPatterns = {"/userPage", "/newOffer", "/newArtwork",
    "/removeAuction", "/newAuction", "/newProfilePicture", "/withdraw", "/deposit", "/notifies"})
public class FilterLogin implements Filter {
  public void init(FilterConfig config) throws ServletException {
  }

  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest newRequest = (HttpServletRequest) request;
    HttpSession session = newRequest.getSession();
    if (session.getAttribute("utente") == null) {
      request.setAttribute("errore", "necessario il login per procedere");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
      return;
    }
    chain.doFilter(request, response);
  }
}
