package it.unisa.c02.moneyart.gestione.vendite.aste.service;

import it.unisa.c02.moneyart.gestione.utente.service.UtenteService;
import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Partecipazione;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.AstaDao;

import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.formattingDates.FormattingDates;
import it.unisa.c02.moneyart.utils.locking.AstaLockingSingleton;
import it.unisa.c02.moneyart.utils.production.Retriever;
import it.unisa.c02.moneyart.utils.timers.TimedObject;
import it.unisa.c02.moneyart.utils.timers.TimerScheduler;

import java.util.Date;
import it.unisa.c02.moneyart.utils.timers.TimerService;

import java.util.List;


public class AstaServiceImpl implements AstaService, TimerService {

  /**
   * Costruttore senza paramentri.
   */
  public AstaServiceImpl() {
    this.astaDao = Retriever.getIstance(AstaDao.class);
    this.operaDao = Retriever.getIstance(OperaDao.class);
    this.utenteDao = Retriever.getIstance(UtenteDao.class);
    this.partecipazioneDao = Retriever.getIstance(PartecipazioneDao.class);
    this.astaLockingSingleton = Retriever.getIstance(AstaLockingSingleton.class);
    this.timerScheduler = Retriever.getIstance(TimerScheduler.class);
    this.utenteService = Retriever.getIstance(UtenteService.class);
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
   *                             ♦   * @param utenteService        permette di accedere alle funzionalità
   *                             del sottositema per la gestione degli utenti
   */
  public AstaServiceImpl(AstaDao astaDao,
                         OperaDao operaDao,
                         UtenteDao utenteDao,
                         PartecipazioneDao partecipazioneDao,
                         TimerScheduler timerScheduler,
                         AstaLockingSingleton astaLockingSingleton,
                         UtenteService utenteService) {
    this.astaDao = astaDao;
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.partecipazioneDao = partecipazioneDao;
    this.timerScheduler = timerScheduler;
    this.astaLockingSingleton = astaLockingSingleton;
    this.utenteService = utenteService;
  }

  /**
   * Restituisce tutte le informazioni relative ad un asta.
   *
   * @param id l'identificativo dell'asta
   * @return l'asta identificata dall'id
   */
  @Override
  public Asta getAuction(int id) {
    return astaDao.doRetrieveById(id);
  }

  /**
   * Restituisce tutte le aste esistenti sulla piattaforma.
   *
   * @return la lista di tutte le aste presenti sulla piattaforma.
   */
  @Override
  public List<Asta> getAllAuctions() {
    return astaDao.doRetrieveAll("id");
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
      if (!asta.getStato().equals(Asta.Stato.IN_CORSO) || utente.getSaldoDisponibile() < offerta ||
          (currentBestOffer != null && offerta <= currentBestOffer.getOfferta())) {
        return false;
      }
      //creo la nuova offerta
      Partecipazione nuovaOfferta = new Partecipazione(asta, utente, offerta);
      //riduco il saldo disponibile dell'offerente
      utente.setSaldoDisponibile(utente.getSaldoDisponibile() - offerta);
      //ripristino il saldo disponibile del vecchio miglior offerente
      if (currentBestOffer != null) {
        Utente oldBestBidder = utenteDao.doRetrieveById(currentBestOffer.getUtente().getId());
        oldBestBidder.setSaldoDisponibile(
            oldBestBidder.getSaldoDisponibile() + currentBestOffer.getOfferta());
        utenteDao.doUpdate(oldBestBidder);
        //qui in teoria c'è anche la questione degli observer

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
    asta.setDataInizio(FormattingDates.setMidnightTime(
        asta.getDataInizio())); //Il giorno di inizio di un'asta parte sempre da mezzanotte
    astaDao.doCreate(asta);
    TimedObject timedObject = new TimedObject(asta.getId(), "avviaAsta", asta.getDataInizio());
    timerScheduler.scheduleTimedService(timedObject);
    return true;
  }

  /**
   * Rimuove un'asta.
   *
   * @param asta l'asta da rimuovere
   * @return vero se la rimozione è andata a buon fine, falso altrimenti
   */
  @Override
  public boolean removeAsta(Asta asta) {
    astaDao.doDelete(asta);
    return true;
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
        utenteService.transfer(vincitore, artista, (float) miglioreOfferta.getOfferta());

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

  private TimerScheduler timerScheduler;
  private AstaLockingSingleton astaLockingSingleton;

  private UtenteService utenteService;
}
