package it.unisa.c02.moneyart.gestione.vendite.aste.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Notifica;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;

import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.production.Retriever;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;

import java.util.ArrayList;
import java.util.Date;
import it.unisa.c02.moneyart.utils.timers.TimerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AstaServiceImpl implements AstaService, TimerService {

  /**
   * Costruttore senza paramentri.
   */
  public AstaServiceImpl() {
    this.astaDao = Retriever.getInstance(AstaDao.class);
    this.operaDao = Retriever.getInstance(OperaDao.class);
    this.utenteDao = Retriever.getInstance(UtenteDao.class);
    this.notificaDao = Retriever.getInstance(NotificaDao.class);
    this.partecipazioneDao = Retriever.getInstance(PartecipazioneDao.class);
    this.astaLockingSingleton = Retriever.getInstance(AstaLockingSingleton.class);
    this.timerScheduler = Retriever.getInstance(TimerScheduler.class);
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
    if (asta == null) {
      return null;
    }
    asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
    return asta;
  }

  /**
   * Restituisce tutte le aste esistenti sulla piattaforma.
   *
   * @return la lista di tutte le aste presenti sulla piattaforma.
   */
  @Override
  public List<Asta> getAllAuctions() {
    List<Asta> aste = astaDao.doRetrieveAll("id");
    if (aste == null) {
      return null;
    }
    for (Asta asta : aste) {
      asta.setOpera(operaDao.doRetrieveById(asta.getOpera().getId()));
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
    }
    return aste;
  }

  /**
   * Permette ad un utente di partecipare ad un asta.
   * Precondizine: L'Asta non deve essere terminata,
   * l'utente deve avere un saldo sufficente per effettuare l'offerta e
   * L'offerta è superiore a quella attuale dell'asta
   * Postcondizione: l'offerta viene registrata
   *
   * @param utente  l'utente che vuole effettuare l'offerta
   * @param asta    l'asta per cui si vuole effetuare l'offerta
   * @param offerta l'offerta fatta dall'utente
   * @return vero se l'offerta va a buon fine, falso altrimenti
   */
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
      if (!asta.getStato().equals(Asta.Stato.IN_CORSO) || utente.getSaldo() < offerta ||
          (currentBestOffer != null && offerta <= currentBestOffer.getOfferta())) {
        return false;
      }
      //creo la nuova offerta
      Partecipazione nuovaOfferta = new Partecipazione(asta, utente, offerta);
      //riduco il saldo dell'offerente
      utente.setSaldo(utente.getSaldo() - offerta);
      //ripristino il saldo disponibile del vecchio miglior offerente
      if (currentBestOffer != null) {
        Utente oldBestBidder = utenteDao.doRetrieveById(currentBestOffer.getUtente().getId());
        oldBestBidder.setSaldo(
            oldBestBidder.getSaldo() + currentBestOffer.getOfferta());
        utenteDao.doUpdate(oldBestBidder);
        Notifica notifica =
            new Notifica(oldBestBidder, asta, null, Notifica.Tipo.SUPERATO, "", false);
        notificaDao.doCreate(notifica);

      }
      //aggiorno i dati perstistenti
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

  private boolean checkDate(Date startDate, Date endDate) {
    long millis = System.currentTimeMillis();
    Date today = new Date(millis);

    return (startDate.after(today) || startDate.equals(today)) && endDate.after(startDate);

  }


  /**
   * Aggiunge una nuova asta.
   *
   * @param asta l'asta da aggiungere
   * @return vero se l'aggiunta è andata a buon fine, falso altrimenti
   */
  @Override
  public boolean addAsta(Asta asta) {

    if (!checkDate(asta.getDataInizio(), asta.getDataFine())) {
      return false;
    }
    asta.setStato(Asta.Stato.CREATA);
    asta.setDataInizio(setMidnightTime(
        asta.getDataInizio())); //Il giorno di inizio di un'asta parte sempre da mezzanotte
    astaDao.doCreate(asta);
    TimedObject timedObject = new TimedObject(asta.getId(), "avviaAsta", asta.getDataInizio());
    timerScheduler.scheduleTimedService(timedObject);
    return true;
  }

  /* La data viene restituita con il tempo impostato a mezzanotte */
  private Date setMidnightTime(Date date) {
    if (date == null) {
      return null;
    }


    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);

    return date;
  }

  /**
   * Rimuove un'asta.
   *
   * @param asta l'asta da rimuovere
   * @return vero se la rimozione è andata a buon fine, falso altrimenti
   */
  @Override
  public boolean removeAsta(Asta asta) {
    if (asta.getStato().equals(Asta.Stato.IN_CORSO) || asta.getStato().equals(Asta.Stato.CREATA)) {
      astaAnnullata(asta);
      Opera opera = operaDao.doRetrieveById(asta.getOpera().getId());
      Utente artista = utenteDao.doRetrieveById(opera.getArtista().getId());
      Notifica notifica = new Notifica(artista, asta, null, Notifica.Tipo.ANNULLAMENTO, "", false);
      notificaDao.doCreate(notifica);
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
   * effettua tutte le modifiche causate dall'annullamento di un asta.
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
          new Notifica(offerente, asta, null, Notifica.Tipo.ANNULLAMENTO, "", false);
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
    Partecipazione bestOffer = partecipazioni.get(0);
    for (Partecipazione partecipazione : partecipazioni) {
      if (partecipazione.getOfferta() > bestOffer.getOfferta()) {
        bestOffer = partecipazione;
      }
    }
    return bestOffer;
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
      if (asta.getStato().equals(Asta.Stato.TERMINATA) &&
          bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
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
      if (asta.getStato().equals(Asta.Stato.TERMINATA) &&
          !bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
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
      if (asta.getStato().equals(Asta.Stato.IN_CORSO) &&
          bestOffer(asta).getId().equals(partecipazione.getId())) {
        aste.add(asta);
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
        Notifica notifica = new Notifica(vincitore, asta, null, Notifica.Tipo.VITTORIA, "", false);
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

  @Override
  public void executeTimedTask(TimedObject item) {
    Asta asta = astaDao.doRetrieveById((Integer) item.getAttribute());
    if (item.getTaskType().equals("avviaAsta")) {
      avviaAsta(asta);
    } else if (item.getTaskType().equals("terminaAsta")) {
      terminaAsta(asta);
    }

  }


  private AstaDao astaDao;
  private OperaDao operaDao;
  private UtenteDao utenteDao;
  private PartecipazioneDao partecipazioneDao;
  private NotificaDao notificaDao;

  private TimerScheduler timerScheduler;
  private AstaLockingSingleton astaLockingSingleton;

}
