package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;


import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.RivenditaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;

/**
 * Classe che implementa i metodi dell'interfaccia RivenditaService.
 */

public class RivenditaServiceImpl implements RivenditaService {


  /**
   * Costruttore senza paramentri.
   */
  public RivenditaServiceImpl() {

  }

  /**
   * Costruttore della classe.
   *
   * @param operaDao     dao di un'opera
   * @param utenteDao    dao di un utente
   * @param rivenditaDao dao di un utente
   */
  public RivenditaServiceImpl(UtenteDao utenteDao, OperaDao operaDao, RivenditaDao rivenditaDao,
                              NotificaDao notificaDao, AstaDao astaDao,
                              PartecipazioneDao partecipazioneDao) {
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.rivenditaDao = rivenditaDao;
    this.notificaDao = notificaDao;
    this.astaDao = astaDao;
    this.partecipazioneDao = partecipazioneDao;

  }

  /**
   * Restituisce tutte le informazioni relative ad una rivendita.
   *
   * @param id l'identificativo della rivendita
   * @return la rivendita identificata dall'id
   */
  @Override
  public Rivendita getResell(Integer id) {
    Rivendita rivendita = rivenditaDao.doRetrieveById(id);

    Opera opera = operaDao.doRetrieveById(rivendita.getOpera().getId());
    opera.setArtista(utenteDao.doRetrieveById((opera.getArtista().getId())));
    opera.getArtista().setnFollowers(getNumberOfFollowers(opera.getArtista()));

    opera.setPossessore(utenteDao.doRetrieveById(opera.getPossessore().getId()));

    rivendita.setOpera(opera);

    return rivendita;
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

    opera = operaDao.doRetrieveById(opera.getId());
    Asta asta = new Asta();
    asta.setId(-1);
    for (Asta asta1 : astaDao.doRetrieveByOperaId(opera.getId())) {
      if (asta1.getStato().equals(Asta.Stato.TERMINATA)) {
        asta = asta1;
        break;
      }
    }

    List<Partecipazione> partecipazioni = partecipazioneDao.doRetrieveAllByAuctionId(asta.getId());
    if (partecipazioni.size() == 0) {
      return 0;
    }
    Partecipazione partecipazione =
        partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()).get(partecipazioni.size() - 1);
    double prezzo = partecipazione.getOfferta();
    Utente artista = utenteDao.doRetrieveById(opera.getArtista().getId());
    int followers = getNumberOfFollowers(artista);
    prezzo += prezzo * (followers / 10000.0);
    return prezzo;
  }


  /**
   * Pone un'opera posseduta dall'utente in rivendita.
   *
   * @param idOpera identificativo dell'opera da mettere in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   * @pre opera.stato = IN_POSSESSO
   * @post Rivendita.allIStances() -> size() = @pre Rivendita.allIStances() -> size() + 1
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
   * Realizza la funzionalit?? di acquisto diretto di un'opera.
   *
   * @param idRivendita identificativo della rivendita
   * @param idUtente    identificativo dell'utente che vuole acquistare l'opera in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   * @pre utente.saldo > rivendita.saldo
   * @post opera.getProprietario() = utente
   */
  @Override
  public boolean buy(Integer idRivendita, Integer idUtente) {
    Rivendita rivendita = rivenditaDao.doRetrieveById(idRivendita);
    Utente utente = utenteDao.doRetrieveById(idUtente);

    if (rivendita == null || utente == null
        || !rivendita.getStato().equals(Rivendita.Stato.IN_CORSO)
        || utente.getSaldo() < rivendita.getPrezzo()) {
      return false;
    }
    Opera opera = operaDao.doRetrieveById(rivendita.getOpera().getId());
    Utente owner = utenteDao.doRetrieveById(opera.getPossessore().getId());
    utente.setSaldo(utente.getSaldo() - rivendita.getPrezzo());
    owner.setSaldo(owner.getSaldo() + rivendita.getPrezzo());
    opera.setPossessore(utente);
    opera.setStato(Opera.Stato.IN_POSSESSO);

    rivendita.setStato(Rivendita.Stato.TERMINATA);
    Notifica notifica =
        new Notifica(owner, new Asta(), rivendita, Notifica.Tipo.TERMINATA, "", false);

    notificaDao.doCreate(notifica);
    utenteDao.doUpdate(utente);
    utenteDao.doUpdate(owner);
    operaDao.doUpdate(opera);
    rivenditaDao.doUpdate(rivendita);

    return true;
  }

  /**
   * Restituisce tutte le rivendite.
   *
   * @return tutte le rivendite
   */
  @Override
  public List<Rivendita> getResells() {
    List<Rivendita> rivendite = rivenditaDao.doRetrieveAll("");

    for (Rivendita rivendita : rivendite) {
      Opera opera = operaDao.doRetrieveById(rivendita.getOpera().getId());
      opera.setArtista(utenteDao.doRetrieveById((opera.getArtista().getId())));
      opera.getArtista().setnFollowers(getNumberOfFollowers(opera.getArtista()));
      opera.setPossessore(utenteDao.doRetrieveById(opera.getPossessore().getId()));

      rivendita.setOpera(opera);
    }

    return rivendite;
  }

  /**
   * Restituisce tutte le rivendite in un determinato stato.
   *
   * @param stato stato ricercato dall'utente
   * @return rivendite con stato uguale a quello ricercato
   */
  @Override
  public List<Rivendita> getResellsByState(Rivendita.Stato stato) {

    List<Rivendita> rivendite = rivenditaDao.doRetrieveByStato(stato);

    for (Rivendita rivendita : rivendite) {
      Opera opera = operaDao.doRetrieveById(rivendita.getOpera().getId());
      opera.setArtista(utenteDao.doRetrieveById((opera.getArtista().getId())));
      opera.getArtista().setnFollowers(getNumberOfFollowers(opera.getArtista()));
      rivendita.setOpera(opera);
    }

    return rivendite;
  }

  /**
   * Restituisce tutte le rivendite con un determinato stato ordinate in base al prezzo.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s     lo stato della rivendita
   * @return la lista ordinata
   */
  @Override
  public List<Rivendita> getResellsSortedByPrice(String order, Rivendita.Stato s) {
    List<Rivendita> rivendite = getResellsByState(s);

    Collections.sort(rivendite, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        Double price1 = r1.getPrezzo();
        Double price2 = r2.getPrezzo();
        return Double.compare(price1, price2);
      }
    });

    if (order.equalsIgnoreCase("DESC")) {
      Collections.reverse(rivendite);
    }

    return rivendite;
  }

  /**
   * Restituisce tutte le rivendite con un determinato stato ordinate in base
   * alla popolarit?? dell'artista.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s     lo stato della rivendita
   * @return la lista ordinata
   */
  @Override
  public List<Rivendita> getResellsSortedByArtistFollowers(String order, Rivendita.Stato s) {
    List<Rivendita> rivendite = getResellsByState(s);


    Collections.sort(rivendite, new Comparator<Rivendita>() {
      @Override
      public int compare(Rivendita r1, Rivendita r2) {
        int f1 = r1.getOpera().getArtista().getnFollowers();
        int f2 = r1.getOpera().getArtista().getnFollowers();
        return Integer.compare(f1, f2);
      }
    });

    if (order.equalsIgnoreCase("DESC")) {
      Collections.reverse(rivendite);
    }

    return rivendite;
  }

  /**
   * Restituisce il numero di followers di un determinato utente.
   *
   * @param utente l'utente interessato a conoscere il numero dei propri followers
   * @return il numero di followers dell'utente
   */

  private int getNumberOfFollowers(Utente utente) {
    List<Utente> followers = utenteDao.doRetrieveFollowersByUserId(utente.getId());

    return followers.size();
  }

  @Inject
  private UtenteDao utenteDao;
  @Inject
  private RivenditaDao rivenditaDao;
  @Inject
  private OperaDao operaDao;
  @Inject
  private NotificaDao notificaDao;
  @Inject
  private AstaDao astaDao;
  @Inject
  private PartecipazioneDao partecipazioneDao;

  private static final double VALORE_PER_FOLLOWER = 0.10d;
}
