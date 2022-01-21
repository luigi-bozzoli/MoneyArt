package it.unisa.c02.moneyart.gestione.vendite.rivendite.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.beans.Rivendita;
import java.util.List;

/**
 * Classe che espone i servizi di logica di buisness per un oggetto Rivendita.
 *
 */

public interface RivenditaService {


  /**
   * Calcola il valore di un'opera in base al numero di followers dell'artista dell'opera.
   *
   * @param opera bean dell'opera da valutare
   * @return valore attuale dell'opera
   */
  double getResellPrice(Opera opera);

  /**
   * Pone un'opera posseduta dall'utente in rivendita.
   *
   * @param idOpera identificativo dell'opera da mettere in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   * @pre opera.stato = IN_POSSESSO
   * @post Rivendita.allIStances() -> size() = @pre Rivendita.allIStances() -> size() + 1
   */
  boolean resell(Integer idOpera);

  /**
   * Realizza la funzionalità di acquisto diretto di un'opera.
   *
   * @param idRivendita identificativo della rivendita
   * @param idUtente    identificativo dell'utente che vuole acquistare l'opera in vendita
   * @return true se l'operazione va a buon fine, false altrimenti
   */
  boolean buy(Integer idRivendita, Integer idUtente);

  /**
   * Restituisce tutte le rivendite.
   *
   * @return tutte le rivendite
   */
  List<Rivendita> getResells();

  /**
   * Restituisce tutte le rivendite in un determinato stato.
   *
   * @param stato stato ricercato dall'utente
   * @return rivendite con stato uguale a quello ricercato
   */
  List<Rivendita> getResellsByState(Rivendita.Stato stato);

  /**
   * Restituisce tutte le rivendite con un determinato stato ordinate in base al prezzo.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s lo stato della rivendita
   * @return la lista ordinata
   */
  List<Rivendita> getResellsSortedByPrice(String order, Rivendita.Stato s);

  /**
   * Restituisce tutte le rivendite con un determinato stato ordinate in base
   * alla popolarità dell'artista.
   *
   * @param order ASC = ordinato in senso crescente, DESC in senso decrescente
   * @param s lo stato della rivendita
   * @return la lista ordinata
   */
  List<Rivendita> getResellsSortedByArtistFollowers(String order, Rivendita.Stato s);

  /**
   * Restituisce tutte le informazioni relative ad una rivendita.
   *
   * @param id l'identificativo della rivendita
   * @return la rivendita identificata dall'id
   */
  Rivendita getResell(Integer id);

}
