package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

public interface UtenteService {

  Utente checkUser(String username, String password);

  Utente getUserInformation(int id);

  boolean signUpUser(Utente utente);

  void updateUser(Utente utente);

  List<Utente> getAllUsers();

  List<Utente> searchUsers(String txt);

  boolean checkUsername(String username);

  boolean checkEmail(String email);

  boolean follow(Utente follower, Utente followed);

  boolean unfollow(Utente follower);

  int getNumberOfFollowers(Utente utente);

  boolean deposit(Utente utente, float amount);

  boolean withdraw(Utente utente, float amount);

  float getBalance(Utente utente);

  boolean transfer(Utente sender, Utente receiver, float amount);

  byte[] encryptPassword(String password);





}
