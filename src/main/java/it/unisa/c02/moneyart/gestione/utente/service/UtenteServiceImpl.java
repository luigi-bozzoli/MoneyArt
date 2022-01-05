package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtenteServiceImpl implements UtenteService {


  public UtenteServiceImpl() {
    this.utenteDao = Retriever.getIstance(UtenteDao.class);
    this.operaDao = Retriever.getIstance(OperaDao.class);
    this.notificaDao = Retriever.getIstance(NotificaDao.class);
    this.partecipazioneDao = Retriever.getIstance(PartecipazioneDao.class);
  }

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
   * @param password
   * @return il bean utente se sono state trovate le credenziali nel database,
 *           null altrimenti
   */
  @Override
  public Utente checkUser(String username, String password) {
    byte[] pswC = encryptPassword(password);

    Utente utente;

    if(!validateEmail(username)) {
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
   * @param id id dell'utente
   * @returnil bean utente se sono state trovate le credenziali nel database,
   *           null altrimenti
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
    if(checkEmail(utente.getEmail()) || checkUsername(utente.getUsername())) {
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
    Utente oldData = utenteDao.doRetrieveByUsername(utente.getUsername());

    if(utente.getFotoProfilo() == null) {
      utente.setFotoProfilo(oldData.getFotoProfilo());
    }
    utente.setId(oldData.getId());
    utenteDao.doUpdate(utente);


  }

  @Override
  public List<Utente> getAllUsers() {
    return null;
  }

  /**
   * Verifica se esiste un utente nel database con uno username specifico.
   *
   * @param username l'username da ricercare nel database
   * @return true se è stato trovato un altro utente con lo stesso username,
   *         false altrimenti
   */
  @Override
  public boolean checkUsername(String username) {
    Utente utente = utenteDao.doRetrieveByUsername(username);
    if(utente != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Verifica se esiste un utente nel database con una email specifica.
   *
   * @param email l'email da ricercare nel database
   * @return true se è stato trovato un altro utente con la stessa email,
   *         false altrimenti
   */
  @Override
  public boolean checkEmail(String email) {
    Utente utente = utenteDao.doRetrieveByEmail(email);
    if(utente != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Permette ad un utente di seguire un artista (se non segue già qualcun'altro).
   *
   * @param follower l'utente che intende seguire un artista
   * @param followed l'artista da seguire
   * @return true se l'utente segue con successo l'artista, false se
   *         l'utente segue già un altro artista
   */
  @Override
  public boolean follow(Utente follower, Utente followed) {
    followed = utenteDao.doRetrieveByUsername(followed.getUsername());

    if(follower.getSeguito() == null) {
      follower.setSeguito(followed);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Permette ad un utente di cancellare il follow da un artista
   *
   * @param follower l'artista da smettere di seguire.
   * @return true se l'utente smette di seguire con successo un artista,
   *         false se l'utente già non seguiva nessuno
   */
  @Override
  public boolean unfollow(Utente follower) {
    if(follower.getSeguito() == null) {
      return false;
    } else {
      follower.setSeguito(null);
      return true;
    }
  }

  /**
   * Restituisce il numero di followers di un determinato utente.
   *
   * @param utente l'utente interessato a conoscere il numero dei propri followers
   * @return il numero di followers dell'utente
   */
  @Override
  public int getNumberOfFollowers(Utente utente) {
    utente = utenteDao.doRetrieveByUsername(utente.getUsername());

    List<Utente> followers = utenteDao.doRetrieveFollowersByUserId(utente.getId());

    return followers.size();
  }

  @Override
  public boolean deposit(Utente utente, double amount) {
    return false;
  }

  @Override
  public boolean withdraw(Utente utente, double amount) {
    return false;
  }

  @Override
  public boolean transfer(Utente sender, Utente receiver, double amount) {
    return false;
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

  private UtenteDao utenteDao;

  private OperaDao operaDao;

  private NotificaDao notificaDao;

  private PartecipazioneDao partecipazioneDao;

  private final Pattern VALID_EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
