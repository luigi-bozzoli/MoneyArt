package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;

import java.util.List;

public interface RivenditaService {

  double getResellPrice(Opera opera);

  boolean resell(Integer idOpera);

  boolean buy(Integer idRivendita, Integer idUtente);

  List<Rivendita> getResells(String stato);

  Rivendita getResell(Integer id);
}
