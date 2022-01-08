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
   * @param operaDao     dao di un'opera
   * @param utenteDao    dao di un utente
   * @param rivenditaDao dao di un utente
   */
  public RivenditaServiceImpl(UtenteDao utenteDao, OperaDao operaDao, RivenditaDao rivenditaDao) {
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.rivenditaDao = rivenditaDao;

  }

  @Override
  public Rivendita getResell(Integer id) {
    return rivenditaDao.doRetrieveById(id);
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
      return 0;
    }

    double prezzo = 0;
    Utente artista = opera.getArtista();
    Integer followers = utenteDao.doRetrieveFollowersByUserId(artista.getId()).size();
    prezzo = followers * prezzo;
    return prezzo;
  }

  /**
   * Pone un'opera posseduta dall'utente in rivendita.
   *
   * @param idOpera identificativo dell'opera da mettere in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   */
  @Override
  public boolean resell(Integer idOpera) {
    Opera opera = operaDao.doRetrieveById(idOpera);
    if (opera == null) {
      return false;
    }

    if (opera.getStato().equals(Opera.Stato.IN_POSSESSO)) {
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
   * @param idRivendita identificativo della rivendita
   * @param idUtente    identificativo dell'utente che vuole acquistare l'opera in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   */
  @Override
  public boolean buy(Integer idRivendita, Integer idUtente) {
    Rivendita rivendita = rivenditaDao.doRetrieveById(idRivendita);
    Utente utente = utenteDao.doRetrieveById(idUtente);

    if (rivendita == null || utente == null ||
        !rivendita.getStato().equals(Rivendita.Stato.IN_CORSO) ||
        utente.getSaldoDisponibile() < rivendita.getPrezzo()) {
      return false;
    }
    Opera opera = operaDao.doRetrieveById(rivendita.getOpera().getId());
    opera.setPossessore(utente);
    opera.setStato(Opera.Stato.IN_POSSESSO);
    Utente owner = utenteDao.doRetrieveById(opera.getPossessore().getId());
    utente.setSaldo(utente.getSaldo() - rivendita.getPrezzo());
    utente.setSaldoDisponibile(utente.getSaldoDisponibile() - rivendita.getPrezzo());
    owner.setSaldo(owner.getSaldo() + rivendita.getPrezzo());

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
