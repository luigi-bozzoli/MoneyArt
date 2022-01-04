package it.unisa.c02.moneyart.gestione.utente.service;

import it.unisa.c02.moneyart.model.beans.Utente;
import java.util.List;

public interface UtenteService {

  Utente checkUser(String username, String password);

  Utente getUserInformation(int id);

  boolean signUpUser(Utente utente);

  Utente updateUser(Utente utente);

  List<Utente> getAllUsers();

  boolean checkUsername(String username);

  boolean checkEmail(String email);

  boolean follow(Utente follower, Utente followed);

  boolean unfollow(Utente follower);

  boolean deposit(Utente utente, double amount);

  boolean withdraw(Utente utente, double amount);

  boolean transfer(Utente sender, Utente receiver, double amount);





}
