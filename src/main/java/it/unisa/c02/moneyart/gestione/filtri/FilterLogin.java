package it.unisa.c02.moneyart.gestione.filtri;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Classe che filtra un utente.
 */

@WebFilter(filterName = "FilterLogin", urlPatterns = {"/userPage", "/newArtwork",
    "/cancelAuction", "/newAuction", "/newProfilePicture", "/wallet", "/walletInfo", "/notifies",
    "/getUserArtworks", "/follow", "/changeUserInformation", "/userAuctions",
    "/buyArtwork", "/resell", "/pay", "/recivePayment", "/pages/notifiche.jsp",
    "/deleteNotification", "/addReport", "/servletCheckout", "/pages/opereUtente.jsp",
    "/pages/asteCreateUtente.jsp", "/pages/rivenditeUtente.jsp"})
public class FilterLogin implements Filter {
  public void init(FilterConfig config) {
  }

  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest newRequest = (HttpServletRequest) request;
    HttpSession session = newRequest.getSession();
    if (session.getAttribute("utente") == null) {
      request.setAttribute("error", "necessario il login per procedere");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
      return;
    }
    chain.doFilter(request, response);
  }
}
