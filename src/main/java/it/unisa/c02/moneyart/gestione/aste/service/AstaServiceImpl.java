package it.unisa.c02.moneyart.gestione.aste.service;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
   * Verifica la correttezza delle date inserite durante la fase di creazione dell'asta.
   * Formato data dd/mm/yyyy --> Si assumono le 24:00:00 come orario fisso per i giorni
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
    asta.setDataInizio(FormattingDates.setMidnightTime(asta.getDataInizio())); //Il giorno di inizio di un'asta parte sempre da mezzanotte
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
