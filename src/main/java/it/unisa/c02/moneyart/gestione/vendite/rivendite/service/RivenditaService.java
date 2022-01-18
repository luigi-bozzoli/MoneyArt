package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import java.util.List;

/**
 * Classe che espone i servizi di logica di buisness per un oggetto Rivendita.
 *
 */

public interface RivenditaService {

  double getResellPrice(Opera opera);

  boolean resell(Integer idOpera);

  boolean buy(Integer idRivendita, Integer idUtente);

  List<Rivendita> getResells();

  List<Rivendita> getResellsByState(Rivendita.Stato stato);

  List<Rivendita> getResellsSortedByPrice(String order, Rivendita.Stato s);

  List<Rivendita> getResellsSortedByArtistFollowers(String order, Rivendita.Stato s);

  Rivendita getResell(Integer id);

}
