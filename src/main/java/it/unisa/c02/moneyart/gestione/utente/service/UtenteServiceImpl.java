package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Utente;
import it.unisa.c02.moneyart.model.dao.interfaces.NotificaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.PartecipazioneDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

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

  @Override
  public Utente checkUser(String username, String password) {
    byte[] pswC = null;
    try {
      MessageDigest criptarino = MessageDigest.getInstance("SHA-256");
      pswC = criptarino.digest(password.getBytes());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    Utente utente = utenteDao.doRetrieveByUsername(username);
    if (utente != null && Arrays.equals(utente.getPassword(), pswC)) {
      return utente;
    } else {
      return null;
    }
  }

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

  @Override
  public boolean signUpUser(Utente utente) {
    return false;
  }

  @Override
  public Utente updateUser(Utente utente) {
    return null;
  }

  @Override
  public List<Utente> getAllUsers() {
    return null;
  }

  @Override
  public boolean checkUsername(String username) {
    return false;
  }

  @Override
  public boolean checkEmail(String email) {
    return false;
  }

  @Override
  public boolean follow(Utente follower, Utente followed) {
    return false;
  }

  @Override
  public boolean unfollow(Utente follower) {
    return false;
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


  private UtenteDao utenteDao;

  private OperaDao operaDao;

  private NotificaDao notificaDao;

  private PartecipazioneDao partecipazioneDao;

}
