package it.unisa.c02.moneyart.gestione.vendite.aste.service;

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
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.production.Sing;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;
import it.unisa.c02.moneyart.utils.timers.TimerService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Classe che implementa i metodi dell'interfaccia AstaService.
 */

public class AstaServiceImpl implements AstaService, TimerService {

  /**
   * Costruttore senza paramentri.
   */
  public AstaServiceImpl() {
  }

  /**
   * Costruttore.
   *
   * @param astaDao              il dao per accedere agli oggetti
   *                             Asta memorizzati in modo peristente
   * @param operaDao             il dao per accedere agli oggetti
   *                             Opera memorizzati in modo peristente
   * @param utenteDao            il dao per accedere agli oggetti
   *                             Utente memorizzati in modo peristente
   * @param partecipazioneDao    il dao per accedere agli oggetti
   *                             Partecipazione memorizzati in modo peristente
   * @param timerScheduler       permette di attivare servizi allo
   *                             scadere di un timer
   * @param astaLockingSingleton permette di serializzare l'accesso ad un asta
   * @param notificaDao          il dao per accedere agli oggetti notifica
   */
  public AstaServiceImpl(AstaDao astaDao,
                         OperaDao operaDao,
                         UtenteDao utenteDao,
                         PartecipazioneDao partecipazioneDao,
                         TimerScheduler timerScheduler,
                         AstaLockingSingleton astaLockingSingleton,
                         NotificaDao notificaDao) {
    this.astaDao = astaDao;
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.partecipazioneDao = partecipazioneDao;
    this.timerScheduler = timerScheduler;
    this.astaLockingSingleton = astaLockingSingleton;
    this.notificaDao = notificaDao;
  }

  /**
   * Restituisce tutte le informazioni relative ad un asta.
   *
   * @param id l'identificativo dell'asta
   * @return l'asta identificata dall'id
   */
  @Override
  public Asta getAuction(int id) {
    Asta asta = astaDao.doRetrieveById(id);
    if (asta != null) {
      asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
      asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
      asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
      asta.getOpera().getArtista().setnFollowers(
          getNumberOfFollowers(asta.getOpera().getArtista())
      );
    }
    return asta;
  }

  /**
   * Restituisce tutte le aste esistenti sulla piattaforma.
   *
   * @return la lista di tutte le aste presenti sulla piattaforma.
   */
  @Override
  public List<Asta> getAllAuctions() {
    List<Asta> aste = astaDao.doRetrieveAll(null);
    if (aste != null) {
      for (Asta asta : aste) {
        asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
        asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
        asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
        asta.getOpera().getArtista().setnFollowers(
            getNumberOfFollowers(asta.getOpera().getArtista())
        );
      }
    }
    return aste;
  }

  /**
   * Restituisce tutte le aste con un determinato stato ordinate in base al prezzo.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s     lo stato della rivendita
   * @return la lista ordinata
   */
  @Override
  public List<Asta> getAuctionsSortedByPrice(String order, Asta.Stato s) {

    List<Asta> aste = getAuctionsByState(s);

    aste.sort((a1, a2) -> {
      Double maxA1 =
          a1.getPartecipazioni().size() > 0
          ? a1.getPartecipazioni().get(a1.getPartecipazioni().size() - 1).getOfferta() : 0;
      Double maxA2 = a2.getPartecipazioni().size() > 0
          ? a2.getPartecipazioni().get(a2.getPartecipazioni().size() - 1).getOfferta() : 0;
      return Double.compare(maxA1, maxA2);
    });

    if (order.equalsIgnoreCase("DESC")) {
      Collections.reverse(aste);
    }

    return aste;

  }

  /**
   * Restituisce tutte le aste con un determinato stato ordinate in base ai follower dell'artista.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s     lo stato della rivendita
   * @return la lista ordinata
   */
  @Override
  public List<Asta> getAuctionsSortedByArtistFollowers(String order, Asta.Stato s) {
    List<Asta> aste = getAuctionsByState(s);

    Collections.sort(aste, new Comparator<Asta>() {
      @Override
      public int compare(Asta a1, Asta a2) {
        int f1 = a1.getOpera().getArtista().getnFollowers();
        int f2 = a2.getOpera().getArtista().getnFollowers();
        return Integer.compare(f1, f2);
      }
    });

    if (order.equalsIgnoreCase("DESC")) {
      Collections.reverse(aste);
    }

    return aste;
  }

  /**
   * Restituisce tutte le aste con un determinato stato ordinate in base alla scadenza.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s     lo stato della rivendita
   * @return la lista ordinata
   */
  @Override
  public List<Asta> getAuctionsSortedByExpirationTime(String order, Asta.Stato s) {
    List<Asta> aste = getAuctionsByState(s);

    Collections.sort(aste, new Comparator<Asta>() {
      @Override
      public int compare(Asta a1, Asta a2) {
        Date d1 = a1.getDataFine();
        Date d2 = a2.getDataFine();
        if (d1.before(d2)) {
          return -1;
        } else if (d1.after(d2)) {
          return 1;
        } else {
          return 0;
        }
      }
    });

    if (order.equalsIgnoreCase("DESC")) {
      Collections.reverse(aste);
    }

    return aste;
  }

  /**
   * Restituisce le aste sulla piattaforma che si trovano in un determinato stato.
   *
   * @param s stato delle aste da restituire
   * @return la lista di tutte le aste presenti sulla piattaforma in un determinato stato.
   */
  public List<Asta> getAuctionsByState(Asta.Stato s) {
    List<Asta> aste = astaDao.doRetrieveByStato(s);
    if (aste == null) {
      return null;
    }
    for (Asta asta : aste) {
      asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
      asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
      asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
      asta.getOpera().getArtista().setnFollowers(
          getNumberOfFollowers(asta.getOpera().getArtista())
      );
    }
    return aste;
  }

  /**
   * Permette di partecipare ad un asta facendo un offerta
   * , crea una notifica per informare il vecchio migliore offerte, se esiste.
   *
   * @param utente  l'utente che vuole effettuare l'offerta
   * @param asta    l'asta per cui si vuole effettuare l'offerta
   * @param offerta l'offerta fatta dall'utente
   * @return vero se l'offerta va a buon fine, falso altrimenti
   * @pre asta.getStato() = IN_CORSO and utente.getSaldo() >= offerta
   *      and (bestOffer(asta) = null or offerta >= bestOffer(asta).getOfferta() )
   * @post bestOffer(asta).getOfferta() = offerta
   *       and bestOffer(asta).getUtente() = utente
   *       and (@pre.bestOffer(asta).getUtente() = null
   *       or notifica.allIstances() -> exists(n:notifica | notifica.getAsta(asta)
   *       and notifica.getUtente() = @pre.bestOffer(asta).getUtente() ) )
   */

  // TODO: qui va messo "participate"
  @Override
  public boolean partecipateAuction(Utente utente, Asta asta, double offerta) {
    astaLockingSingleton.lockAsta(asta);
    try {
      //prendo la versione aggiornata di asta e utente
      utente = utenteDao.doRetrieveById(utente.getId());
      asta = astaDao.doRetrieveById(asta.getId());

      //prendo la migliore offerta attuale
      Partecipazione currentBestOffer = bestOffer(asta);

      //controllo le precondizioni
      if (!asta.getStato().equals(Asta.Stato.IN_CORSO) || utente.getSaldo() < offerta
          || (currentBestOffer != null && offerta <= currentBestOffer.getOfferta())) {
        return false;
      }

      //creo la nuova offerta
      Partecipazione nuovaOfferta = new Partecipazione(asta, utente, offerta);
      //asta.getPartecipazioni().add(nuovaOfferta);

      //ripristino il saldo disponibile del vecchio miglior offerente
      if (currentBestOffer != null) {
        Utente oldBestBidder = utenteDao.doRetrieveById(currentBestOffer.getUtente().getId());
        oldBestBidder.setSaldo(oldBestBidder.getSaldo() + currentBestOffer.getOfferta());

        // Caso in cui il miglior offerente vuole alzare
        // la sua offerta
        if (oldBestBidder.getId() == utente.getId()) {
          utente = oldBestBidder;
        } else {
          utenteDao.doUpdate(oldBestBidder);
        }

        Notifica notifica = new Notifica(
            oldBestBidder,
            asta,
            new Rivendita(),
            Notifica.Tipo.SUPERATO,
            "La tua offerta è stata superata.",
            false
        );

        notificaDao.doCreate(notifica);
      }

      //riduco il saldo dell'offerente
      utente.setSaldo(utente.getSaldo() - offerta);

      //aggiorno i dati persistenti
      partecipazioneDao.doCreate(nuovaOfferta);
      utenteDao.doUpdate(utente);

      return true;
    } finally {
      astaLockingSingleton.unlockAsta(asta);
    }
  }

  /**
   * Verifica la correttezza delle date inserite durante la fase di creazione dell'asta.
   * Formato data dd/mm/yyyy --> Si assumono le 24:00:00 come orario fisso per i giorni
   *
   * @param startDate data di inizio per l'asta
   * @param endDate   data di fine per l'asta
   * @return vero o falso
   */

  // TODO: Controllare se sono null (e aggiornare @pre di addAsta())
  private boolean checkDate(Date startDate, Date endDate) {
    long millis = System.currentTimeMillis();
    Date today = setMidnightTime(new Date(millis));

    return (startDate.after(today) || startDate.equals(today)) && endDate.after(startDate);
  }


  /**
   * Aggiunge una nuova asta.
   *
   * @param asta l'asta da aggiungere
   * @return vero se l'aggiunta è andata a buon fine, falso altrimenti
   * @pre day(asta.getDataInizio ()) >= day(new Date())
   *      and day(asta.getDataFine()) > day(asta.getDataInizio())
   *      and asta.getOpera().getStato() = PREVENDITA
   * @post aste.allIstances() -> includes(asta) and asta.getOpera().getStato() = ALL_ASTA
   */
  @Override
  public boolean addAsta(Asta asta) {
    asta.setDataInizio(setMidnightTime(asta.getDataInizio()));
    asta.setDataFine(setMidnightTime(asta.getDataFine()));
    Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());

    if (!checkDate(asta.getDataInizio(), asta.getDataFine())
        || !opera.getStato().equals(Opera.Stato.PREVENDITA)) {
      return false;
    }
    opera.setStato(Opera.Stato.ALL_ASTA);
    asta.setStato(Asta.Stato.CREATA);
    astaDao.doCreate(asta);
    operaDao.doUpdate(opera);
    TimedObject timedObject = new TimedObject(asta.getId(), "avviaAsta", asta.getDataInizio());
    timerScheduler.scheduleTimedService(timedObject);
    return true;
  }

  /* La data viene restituita con il tempo impostato a mezzanotte */
  private Date setMidnightTime(Date date) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);

    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }

  /**
   * Rimuove un'asta.
   *
   * @param asta l'asta da rimuovere
   * @return vero se la rimozione è andata a buon fine, falso altrimenti
   * @pre asta.getStato() = IN_CORSO or asta.getStato() = CREATA
   * @post asta.allInstances()  -> not includes(asta)
   *       and asta.getOpera().getStato() = PREVENDITA
   *       and ( pre.bestOffer(asta) = null
   *       or notifica.allInstances().doRetrieveAll() ->
   *       Exists(n:notifica | n.getAsta() = asta and n.getUtente = pre.bestOffer(asta).getUtente()
   *       and n.tipo = ANNULLAMENTO))
   */
  @Override
  public boolean removeAsta(Asta asta) {
    if (asta.getStato().equals(Asta.Stato.IN_CORSO) || asta.getStato().equals(Asta.Stato.CREATA)) {
      astaAnnullata(asta);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      Utente artista = utenteDao.doRetrieveById(opera.getArtista().getId());
      Notifica notifica =
          new Notifica(artista, asta, new Rivendita(), Notifica.Tipo.ANNULLAMENTO, "", false);
      notificaDao.doCreate(notifica);
      asta.setStato(Asta.Stato.ELIMINATA);
      astaDao.doUpdate(asta);
      return true;
    } else {
      return false;
    }
  }

  /**
   * annulla un asta.
   *
   * @param asta l'asta da annullare
   * @return vero se l'annullamento è andata a buon fine, falso altrimenti
   * @pre asta.getStato() = IN_CORSO or asta.getStato() = CREATA
   * @post asta.allIstances()  -> not includes(asta)
   *       and asta.getOpera().getStato() = PREVENDITA
   */
  @Override
  public boolean annullaAsta(Asta asta) {
    asta = astaDao.doRetrieveById(asta.getId());

    if (asta.getStato().equals(Asta.Stato.IN_CORSO) || asta.getStato().equals(Asta.Stato.CREATA)) {
      astaAnnullata(asta);
      asta.setStato(Asta.Stato.ELIMINATA);
      astaDao.doUpdate(asta);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Effettua tutte le modifiche causate dall'annullamento o dalla rimozione di un asta.
   * Ripristina il saldo del miglior offerente.
   * imposta lo stato dell'opera.
   * avvisa il miglior offerente della cancellazione
   *
   * @param asta l'asta che è stata annullata
   */
  private void astaAnnullata(Asta asta) {

    Partecipazione bestOffer = bestOffer(asta);
    if (bestOffer != null) {
      Utente offerente = utenteDao.doRetrieveById(bestOffer.getUtente().getId());
      offerente.setSaldo(offerente.getSaldo() + bestOffer.getOfferta());
      utenteDao.doUpdate(offerente);
      Notifica notifica =
          new Notifica(offerente, asta, new Rivendita(), Notifica.Tipo.ANNULLAMENTO, "", false);
      notificaDao.doCreate(notifica);
    }
    Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
    opera.setStato(Opera.Stato.PREVENDITA);
    operaDao.doUpdate(opera);
  }


  /**
   * Restituisce la migliore offerta fatta all'asta.
   *
   * @param asta l'asta da verificare
   * @return la migliore offerta per l'asta o null se non è presente alcuna offerta
   */
  @Override
  public Partecipazione bestOffer(Asta asta) {
    List<Partecipazione> partecipazioni = partecipazioneDao.doRetrieveAllByAuctionId(asta.getId());
    if (partecipazioni == null || partecipazioni.isEmpty()) {
      return null;
    }
    return partecipazioni.get(partecipazioni.size() - 1);
  }

  private List<Partecipazione> bestAuctionOfferByUser(Utente utente) {
    Map<Integer, Partecipazione> maxForAsta = new HashMap<>();
    List<Partecipazione> miglioriPartecipazioni = new ArrayList<>();
    List<Partecipazione> partecipazioni = partecipazioneDao.doRetrieveAllByUserId(utente.getId());
    for (Partecipazione partecipazione : partecipazioni) {
      if (maxForAsta.containsKey(partecipazione.getAsta().getId())) {
        Partecipazione max = maxForAsta.get(partecipazione.getAsta().getId());
        if (max.getOfferta() < partecipazione.getOfferta()) {
          maxForAsta.put(partecipazione.getAsta().getId(), partecipazione);
        }
      } else {
        maxForAsta.put(partecipazione.getAsta().getId(), partecipazione);
      }
    }
    for (Partecipazione partecipazione : maxForAsta.values()) {
      miglioriPartecipazioni.add(partecipazione);
    }
    return miglioriPartecipazioni;
  }

  /**
   * Restituisce tutte le aste vinte da un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste vinte
   * @return le aste vinte dall'utente
   */
  @Override
  public List<Asta> getWonAuctions(Utente utente) {
    List<Partecipazione> partecipazioni = bestAuctionOfferByUser(utente);
    List<Asta> aste = new ArrayList<>();
    for (Partecipazione partecipazione : partecipazioni) {
      Asta asta = astaDao.doRetrieveById(partecipazione.getAsta().getId());
      if (asta.getStato().equals(Asta.Stato.TERMINATA)
          && bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
        asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
        asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
        asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
        asta.getOpera().getArtista().setnFollowers(
            getNumberOfFollowers(asta.getOpera().getArtista())
        );
      }
    }

    return aste;
  }

  /**
   * Restituisce tutte le aste perse da un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste perse
   * @return le aste perse dall'utente
   */
  @Override
  public List<Asta> getLostAuctions(Utente utente) {
    List<Partecipazione> partecipazioni = bestAuctionOfferByUser(utente);
    List<Asta> aste = new ArrayList<>();
    for (Partecipazione partecipazione : partecipazioni) {
      Asta asta = astaDao.doRetrieveById(partecipazione.getAsta().getId());
      if (asta.getStato().equals(Asta.Stato.TERMINATA)
          && !bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
        asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
        asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
        asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
        asta.getOpera().getArtista().setnFollowers(
            getNumberOfFollowers(asta.getOpera().getArtista())
        );
      }
    }

    return aste;
  }

  /**
   * Restituisce tutte le aste in corso di un utente.
   *
   * @param utente l'utente per cui bisogna cercare le aste in corso
   * @return le aste in corso dell'utente
   */
  @Override
  public List<Asta> getCurrentAuctions(Utente utente) {
    List<Partecipazione> partecipazioni = bestAuctionOfferByUser(utente);
    List<Asta> aste = new ArrayList<>();
    for (Partecipazione partecipazione : partecipazioni) {
      Asta asta = astaDao.doRetrieveById(partecipazione.getAsta().getId());
      if (asta.getStato().equals(Asta.Stato.IN_CORSO)
          && bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
        asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
        asta.setPartecipazioni(partecipazioneDao.doRetrieveAllByAuctionId(asta.getId()));
        asta.getOpera().setArtista(utenteDao.doRetrieveById(asta.getOpera().getArtista().getId()));
        asta.getOpera().getArtista().setnFollowers(
            getNumberOfFollowers(asta.getOpera().getArtista())
        );
      }
    }

    return aste;
  }

  /**
   * Restituisce lo storico di tutte le offerte.
   *
   * @return lo storico di tutte le offerte
   */
  @Override
  public List<Partecipazione> getAllOffers() {
    return partecipazioneDao.doRetrieveAll(null);
  }

  private void avviaAsta(Asta asta) {
    if (asta.getStato().equals(Asta.Stato.CREATA)) {
      asta.setStato(Asta.Stato.IN_CORSO);
      astaDao.doUpdate(asta);
      TimedObject timedObject = new TimedObject(asta.getId(), "terminaAsta", asta.getDataFine());
      timerScheduler.scheduleTimedService(timedObject);
    }

  }


  private void terminaAsta(Asta asta) {

    astaLockingSingleton.lockAsta(asta);
    try {
      asta = astaDao.doRetrieveById(asta.getId());
      asta.setStato(Asta.Stato.TERMINATA);

      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());

      Partecipazione miglioreOfferta = bestOffer(asta);
      Utente artista = utenteDao.doRetrieveById(opera.getArtista().getId());
      if (miglioreOfferta == null) {
        opera.setStato(Opera.Stato.PREVENDITA);

      } else {
        Utente vincitore = utenteDao.doRetrieveById(miglioreOfferta.getUtente().getId());
        opera.setStato(Opera.Stato.IN_POSSESSO);
        opera.setPossessore(vincitore);
        artista.setSaldo(artista.getSaldo() + miglioreOfferta.getOfferta());
        Notifica notifica =
            new Notifica(vincitore, asta, new Rivendita(), Notifica.Tipo.VITTORIA, "", false);
        notificaDao.doCreate(notifica);
        utenteDao.doUpdate(vincitore);
        utenteDao.doUpdate(artista);

      }

      astaDao.doUpdate(asta);
      operaDao.doUpdate(opera);


    } finally {
      astaLockingSingleton.unlockAsta(asta);
    }

  }

  /**
   * attiva un evento allo scadere del timer,
   * l'evento può attivare un asta o concluderla.
   *
   * @param item dati riguardanti il timer che si è attivato
   * @pre (item.getTaskType = " avviaAsta "
   *      and (asta.allIstances () -> any(a:asta | asta.getId() = item.attribute())).stato = CREATO)
   *      or
   *      (item.getTaskType = "terminaAsta"
   *      and (bestOffer(asta) = null || asta.Stato = Prevendita)
   *      )
   */
  @Override
  public void executeTimedTask(TimedObject item) {
    Asta asta = astaDao.doRetrieveById((Integer) item.getAttribute());
    if (item.getTaskType().equals("avviaAsta")) {
      avviaAsta(asta);
    } else if (item.getTaskType().equals("terminaAsta")) {
      terminaAsta(asta);
    }

  }

  /**
   * Restituisce il numero di followers di un determinato utente.
   *
   * @param utente l'utente interessato a conoscere il numero dei propri followers
   * @return il numero di followers dell'utente
   */

  private int getNumberOfFollowers(Utente utente) {
    List<Utente> followers = utenteDao.doRetrieveFollowersByUserId(utente.getId());
    return followers != null ? followers.size() : 0;
  }


  @Inject
  private AstaDao astaDao;
  @Inject
  private OperaDao operaDao;
  @Inject
  private UtenteDao utenteDao;
  @Inject
  private PartecipazioneDao partecipazioneDao;
  @Inject
  private NotificaDao notificaDao;

  @Inject
  @Sing
  private TimerScheduler timerScheduler;
  @Inject
  @Sing
  private AstaLockingSingleton astaLockingSingleton;

}
