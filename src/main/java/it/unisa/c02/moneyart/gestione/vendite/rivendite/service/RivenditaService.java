package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import it.unisa.c02.moneyart.model.beans.Utente;

import java.util.List;

public interface RivenditaService {

  double getResellPrice(Opera opera);

  boolean resell(Opera opera);

  boolean buy(Rivendita rivendita, Utente utente);

  List<Rivendita> getResells(String stato);
}
