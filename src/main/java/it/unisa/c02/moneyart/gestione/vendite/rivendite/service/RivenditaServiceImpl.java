package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;

import java.util.List;


public class RivenditaServiceImpl implements RivenditaService {

  /**
   * Costruttore senza paramentri.
   */
  public RivenditaServiceImpl() {
    this.utenteDao = Retriever.getIstance(UtenteDao.class);
    this.operaDao = Retriever.getIstance(OperaDao.class);
    this.rivenditaDao = Retriever.getIstance(RivenditaDao.class);

  }

  /**
   * Costruttore della classe.
   *
   * @param operaDao dao di un'opera
   * @param utenteDao dao di un utente
   * @param rivenditaDao dao di un utente
   */
  public RivenditaServiceImpl(UtenteDao utenteDao, OperaDao operaDao, RivenditaDao rivenditaDao) {
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.rivenditaDao = rivenditaDao;

  }

  /**
   * Calcola il valore di un'opera in base al numero di followers dell'artista dell'opera.
   *
   * @param opera bean dell'opera da valutare
   * @return valore attuale dell'opera
   */
  /*Bisogna decidere come impostare il calcolo del prezzo in base ai followers*/
  @Override
  public double getResellPrice(Opera opera) {
    if (opera == null) {
      return  0;
    }

    double prezzo = 0;
    Utente artist = opera.getArtista();
    Integer followers = utenteDao.doRetrieveFollowersByUserId(artist.getId()).size();
    prezzo = followers * prezzo;
    return prezzo;
  }

  /**
   * Pone un'opera posseduta dall'utente in rivendita.
   *
   * @param opera bean dell'opera da mettere in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   */
  @Override
  public boolean resell(Opera opera) {
    if (opera == null) {
      return false;
    }

    if ((opera.getAste() != null) && opera.getStato().equals(Opera.Stato.IN_POSSESSO)) {
      Rivendita rivendita = new Rivendita();
      rivendita.setOpera(opera);
      rivendita.setStato(Rivendita.Stato.IN_CORSO);
      rivendita.setPrezzo(getResellPrice(opera));
      rivenditaDao.doCreate(rivendita);

      opera.setStato(Opera.Stato.IN_VENDITA);
      operaDao.doUpdate(opera);

      return true;
    }

    return false;
  }

  /**
   * Realizza la funzionalità di acquisto diretto di un'opera.
   *
   * @param rivendita bean della rivendita
   * @param utente bean dell'utente che vuole acquistare l'opera in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   */
  @Override
  public boolean buy(Rivendita rivendita, Utente utente) {
    if (rivendita == null || utente == null) {
      return false;
    }

    Opera opera = rivendita.getOpera();
    opera.setPossessore(utente);
    opera.setStato(Opera.Stato.IN_POSSESSO);
    Utente owner = opera.getPossessore();
    utente.setSaldo(utente.getSaldo() - getResellPrice(opera));
    owner.setSaldo(owner.getSaldo() + getResellPrice(opera));

    rivendita.setStato(Rivendita.Stato.TERMINATA);

    utenteDao.doUpdate(utente);
    utenteDao.doUpdate(owner);
    operaDao.doUpdate(opera);
    rivenditaDao.doUpdate(rivendita);

    return true;
  }

  /**
   * Restituisce tutte le rivendite in un determinato stato.
   *
   * @param stato stato ricercato dall'utente
   * @return rivendite con stato uguale a quello ricercato
   */
  @Override
  public List<Rivendita> getResells(String stato) {
    return rivenditaDao.doRetrieveByStato(stato);
  }

  private UtenteDao utenteDao;
  private RivenditaDao rivenditaDao;
  private OperaDao operaDao;
}
