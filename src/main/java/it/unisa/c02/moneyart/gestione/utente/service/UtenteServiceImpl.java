package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Asta;
import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * Questa classe implementa i metodi dell'interfaccia utenteService.
 */
public class UtenteServiceImpl implements UtenteService {

  /**
   * Costruttore senza paramentri.
   */
  public UtenteServiceImpl() {

  }

  /**
   * Costruttore con paramentri.
   *
   * @param utenteDao         dao dell'utente
   * @param operaDao          dao dell'opera
   * @param notificaDao       dao della notifica
   * @param partecipazioneDao dao della partecipazione
   */
  public UtenteServiceImpl(UtenteDao utenteDao, OperaDao operaDao, NotificaDao notificaDao,
                           PartecipazioneDao partecipazioneDao) {
    this.notificaDao = notificaDao;
    this.operaDao = operaDao;
    this.utenteDao = utenteDao;
    this.partecipazioneDao = partecipazioneDao;
  }

  /**
   * Retituisce un bean utente creato interrogando il database.
   *
   * @param username username o email dell'utente
   * @param password password dell'utente
   * @return il bean utente se sono state trovate le credenziali nel database, null altrimenti
   */
  @Override
  public Utente checkUser(String username, String password) {
    byte[] pswC = encryptPassword(password);

    Utente utente;

    if (!validateEmail(username)) {
      utente = utenteDao.doRetrieveByUsername(username);
    } else {
      utente = utenteDao.doRetrieveByEmail(username);
    }

    if (utente != null && Arrays.equals(utente.getPassword(), pswC)) {
      return utente;
    } else {
      return null;
    }
  }

  /**
   * Restituisce un bean utente creato interrogando il database.
   *
   * @param id id dell'utente
   * @return il bean utente se sono state trovate le credenziali nel database,
   * null altrimenti
   */
  @Override
  public Utente getUserInformation(int id) {
    Utente utente = utenteDao.doRetrieveById(id);
    if (utente == null) {
      return null;
    }
    if (utente.getSeguito().getId() != null) {

      utente.setSeguito(utenteDao.doRetrieveById(utente.getSeguito().getId()));
    }
    utente.setOpereInPossesso(operaDao.doRetrieveAllByOwnerId(utente.getId()));
    utente.setOpereCreate(operaDao.doRetrieveAllByArtistId(utente.getId()));
    utente.setNotifiche(notificaDao.doRetrieveAllByUserId(utente.getId()));
    utente.setPartecipazioni(partecipazioneDao.doRetrieveAllByUserId(utente.getId()));

    utente.setnFollowers(getNumberOfFollowers(utente));
    return utente;

  }
  /**
   * Restituisce un bean utente creato interrogando il database.
   *
   * @param username l'username dell'utente
   * @return il bean utente se sono state trovate le credenziali nel database,
   * null altrimenti
   */
  @Override
  public Utente getUserInformation(String username) {
    Utente utente = utenteDao.doRetrieveByUsername(username);
    if (utente == null) {
      return null;
    }
    if (utente.getSeguito().getId() != null) {

      utente.setSeguito(utenteDao.doRetrieveById(utente.getSeguito().getId()));
    }
    utente.setOpereInPossesso(operaDao.doRetrieveAllByOwnerId(utente.getId()));
    utente.setOpereCreate(operaDao.doRetrieveAllByArtistId(utente.getId()));
    utente.setNotifiche(notificaDao.doRetrieveAllByUserId(utente.getId()));
    utente.setPartecipazioni(partecipazioneDao.doRetrieveAllByUserId(utente.getId()));

    utente.setnFollowers(getNumberOfFollowers(utente));
    return utente;

  }

  /**
   * Inserisci un nuovo utente nel database.
   *
   * @param utente il bean utente
   * @return true se l'inserimento va a buon fine, false altrimenti
   */
  @Override
  public boolean signUpUser(Utente utente) {
    if (checkEmail(utente.getEmail()) || checkUsername(utente.getUsername())) {
      return false;
    } else {
      utenteDao.doCreate(utente);
      return true;
    }
  }

  /**
   * Aggiorna i dati di un utente nel database.
   *
   * @param utente l'utente con i dati aggiornati
   */
  @Override
  public void updateUser(Utente utente) {
    utenteDao.doUpdate(utente);
  }


  /**
   * Restituisce tutti gli utenti presenti nel database.
   *
   * @return la lista di utenti
   */
  @Override
  public List<Utente> getAllUsers() {
    List<Utente> utenti = utenteDao.doRetrieveAll("");

    for (Utente utente : utenti) {
      if (utente.getSeguito().getId() != null) {
        utente.setSeguito(utenteDao.doRetrieveById(utente.getSeguito().getId()));
      }
      utente.setOpereInPossesso(operaDao.doRetrieveAllByOwnerId(utente.getId()));
      utente.setOpereCreate(operaDao.doRetrieveAllByArtistId(utente.getId()));
      utente.setNotifiche(notificaDao.doRetrieveAllByUserId(utente.getId()));
      utente.setPartecipazioni(partecipazioneDao.doRetrieveAllByUserId(utente.getId()));

      utente.setnFollowers(getNumberOfFollowers(utente));
    }

    return utenti;
  }

  /**
   * Restituisce tutte gli utenti ordinati in base ai follower.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @return la lista ordinata
   */
  @Override
  public List<Utente> getUsersSortedByFollowers(String order) {
    List<Utente> utenti = getAllUsers();

    Collections.sort(utenti, new Comparator<Utente>() {
      @Override
      public int compare(Utente u1, Utente u2) {
        return Integer.compare(u1.getnFollowers(), u2.getnFollowers());
      }
    });

    if(order.equalsIgnoreCase("DESC")) {
      Collections.reverse(utenti);
    }

    return utenti;
  }

  /**
   * Restituisce tutti gli utenti nel database che hanno un riscontro con la ricerca.
   *
   * @param txt stringa da ricercare
   * @return una lista di utenti che hanno un riscontro positivo con la ricerca
   */
  @Override
  public List<Utente> searchUsers(String txt) {
    List<Utente> utenti = utenteDao.researchUser(txt);
    return utenti;
  }

  /**
   * Verifica se esiste un utente nel database con uno username specifico.
   *
   * @param username l'username da ricercare nel database
   * @return true se è stato trovato un altro utente con lo stesso username,
   * false altrimenti
   */
  @Override
  public boolean checkUsername(String username) {
    Utente utente = utenteDao.doRetrieveByUsername(username);
    return utente != null;
  }

  /**
   * Verifica se esiste un utente nel database con una email specifica.
   *
   * @param email l'email da ricercare nel database
   * @return true se è stato trovato un altro utente con la stessa email,
   * false altrimenti
   */
  @Override
  public boolean checkEmail(String email) {
    Utente utente = utenteDao.doRetrieveByEmail(email);
    return utente != null;
  }

  /**
   * Permette ad un utente di seguire un artista (se non segue già qualcun'altro).
   *
   * @param follower l'utente che intende seguire un artista
   * @param followed l'artista da seguire
   * @return true se l'utente segue con successo l'artista, false se
   * l'utente segue già un altro artista
   */
  @Override
  public boolean follow(Utente follower, Utente followed) {
    followed = utenteDao.doRetrieveByUsername(followed.getUsername());

    if (follower.getSeguito() == null) {
      follower.setSeguito(followed);
      utenteDao.doUpdate(follower);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Permette ad un utente di cancellare il follow da un artista.
   *
   * @param follower l'utente che vuole smettere di seguire un artista.
   * @return true se l'utente smette di seguire con successo un artista,
   * false se l'utente già non seguiva nessuno
   */
  @Override
  public boolean unfollow(Utente follower) {
    if (follower.getSeguito() == null) {
      return false;
    } else {
      Utente utente = new Utente();
      follower.setSeguito(utente);
      utenteDao.doUpdate(follower);
      return true;
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

    return followers.size();
  }

  /**
   * Permette ad un utente di depositare sul proprio saldo.
   *
   * @param utente l'utente interessato a depositare
   * @param amount l'importo da depositare (da aggiungere al saldo)
   * @return true se il deposito è avvenuto con successo
   * e false se l'amount è inferiore o uguale a zero
   */
  @Override
  public boolean deposit(Utente utente, double amount) {
    if (amount <= 0) {
      return false;
    }
    utente = utenteDao.doRetrieveByUsername(utente.getUsername());
    utente.setSaldo(utente.getSaldo() + amount);
    utenteDao.doUpdate(utente);

    return true;
  }

  /**
   * Permette ad un utente di prelevare dal proprio saldo.
   *
   * @param utente l'utente interessato a prelevare
   * @param amount l'importo da prelevare (da sottrarre al saldo)
   * @return true se il prelievo è avvenuto con successo
   * e false se l'amount è inferiore o uguale a zero
   * e se il saldo dell'utente è minore dell'amount
   */
  @Override
  public boolean withdraw(Utente utente, double amount) {
    utente = utenteDao.doRetrieveByUsername(utente.getUsername());
    if (amount <= utente.getSaldo() && amount > 0) {
      utente.setSaldo(utente.getSaldo() - amount);
      utenteDao.doUpdate(utente);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Restituisce il saldo di un utente.
   *
   * @param utente utente interessato a conoscere il suo saldo
   * @return saldo attuale dell'utente
   */
  @Override
  public double getBalance(Utente utente) {
    utente = utenteDao.doRetrieveByUsername(utente.getUsername());

    return utente.getSaldo();
  }


  /**
   * Restituisce la cifratura in SHA-256 di una stringa.
   *
   * @param password la stringa da cifrare
   * @return la stringa cifrata
   */
  @Override
  public byte[] encryptPassword(String password) {
    byte[] pswC = null;
    try {
      MessageDigest criptarino = MessageDigest.getInstance("SHA-256");
      pswC = criptarino.digest(password.getBytes());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return pswC;
  }


  /**
   * Metodo privato che verifica tramite una regex, se una stringa
   * è del formato email.
   *
   * @param email la stringa da validare
   * @return true se la stringa è una mail, false altrimenti.
   */
  private boolean validateEmail(String email) {
    Matcher matcher = VALID_EMAIL_REGEX.matcher(email);
    return matcher.find();
  }

  @Inject
  private UtenteDao utenteDao;

  @Inject
  private OperaDao operaDao;

  @Inject
  private NotificaDao notificaDao;

  @Inject
  private PartecipazioneDao partecipazioneDao;

  private static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-"
      + "]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
