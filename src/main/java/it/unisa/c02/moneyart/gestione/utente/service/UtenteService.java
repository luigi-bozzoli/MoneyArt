package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

public interface UtenteService {

  Utente checkUser(String username, String password);

  Utente getUserInformation(int id);

  boolean signUpUser(Utente utente);

  void updateUser(Utente utente);

  List<Utente> getAllUsers(String Filter);

  List<Utente> searchUsers(String txt);

  boolean checkUsername(String username);

  boolean checkEmail(String email);

  boolean follow(Utente follower, Utente followed);

  boolean unfollow(Utente follower);

  boolean deposit(Utente utente, double amount);

  boolean withdraw(Utente utente, double amount);

  double getBalance(Utente utente);

  boolean transfer(Utente sender, Utente receiver, double amount);

  byte[] encryptPassword(String password);



}
