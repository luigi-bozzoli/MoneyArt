package it.unisa.c02.moneyart.gestione.opere.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import java.util.List;

/**
 *Classe che espone i servizi di logica di buisness di un oggetto Opera.
 *
 */
public interface OperaService {

  boolean addArtwork(Opera opera);

  boolean checkArtwork(int id, String name);

  Opera getArtwork(int id);

  List<Opera> searchOpera(String name);

  List<Opera> getArtworkByUser(int id);
}
