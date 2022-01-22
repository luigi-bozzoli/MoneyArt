package it.unisa.c02.moneyart.gestione.opere.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import java.util.List;

/**
 *Classe che espone i servizi di logica di buisness di un oggetto Opera.
 *
 */
public interface OperaService {

  /**
   * Inserisce una nuova opera nel database.
   *
   * @param opera bean dell'opera da inserire
   * @return true se l'inserimento va a buon fine,
   *         false altrimenti
   * @pre opera.getNome() <> null AND opera.getImmagine() <> null AND
   *      utente.carica -> forAll(o:Opera | o.getNome() <> opera.getNome())
   * @post utente.carica -> includes(opera)
   */
  boolean addArtwork(Opera opera) throws Exception;

  /**
   * Controlla l'univocità del nome di un'opera, per un determinato artista, presente nel db.
   *
   * @param id   dell'utente artista
   * @param name titolo dell'opera
   * @return true se esiste un'opera con quel nome,
   *         false altrimenti
   * @pre Utente.allIstances() -> exists(u:Utente | id = u.getId()) AND name <> null
   */
  boolean checkArtwork(int id, String name);

  /**
   * Restituisce un bean opera.
   *
   * @param id dell'opera
   * @return del bean opera se presente nel database, null altrimenti
   */
  Opera getArtwork(int id);

  /**
   * Restituisce l'opera cercata nel database.
   *
   * @param name dell'opera da cercare
   * @return una lista di opere avente il nome dell'argomento, null altrimenti
   */
  List<Opera> searchOpera(String name);

  /**
   * Restituisce tutte le opere di un artista.
   *
   * @param id dell'artista
   * @return una lista di opere
   */
  List<Opera> getArtworkByUser(int id);

  /**
   * Restituisce tutte le opere possedute da un utente.
   *
   * @param id dell'utente
   * @return una lista di opere
   */
  List<Opera> getArtworkByOwner(int id);
}
