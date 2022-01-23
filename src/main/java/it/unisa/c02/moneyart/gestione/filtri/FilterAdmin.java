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
 * Classe che filtra un admin.
 *
 */

@WebFilter(filterName = "FilterAdmin", urlPatterns = {"/removeAuction", "/getReports",
    "/readReport", "/removeReport", "/offersHistory", "/adminPage", "/asteAdmin"})
public class FilterAdmin implements Filter {
  public void init(FilterConfig config) throws ServletException {
  }

  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    HttpServletRequest newRequest = (HttpServletRequest) request;
    HttpSession session = newRequest.getSession();
    if (session.getAttribute("admin") == null) {
      request.setAttribute("errore", "necessario il login per procedere");
      RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
      dispatcher.forward(request, response);
      return;
    }
    chain.doFilter(request, response);
  }
}
