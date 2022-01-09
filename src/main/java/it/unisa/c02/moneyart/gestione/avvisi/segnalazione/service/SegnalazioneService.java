package it.unisa.c02.moneyart.gestione.avvisi.segnalazione.service;

import it.unisa.c02.moneyart.model.beans.Segnalazione;

import java.util.List;

public interface SegnalazioneService {

  List<Segnalazione> getReports(String filter);

  Segnalazione getReport(int id);

  void addReport(Segnalazione segnalazione);

  void removeReport(Segnalazione segnalazione);

  void readReport(Segnalazione segnalazione);

  void unreadReport(Segnalazione segnalazione);

}
