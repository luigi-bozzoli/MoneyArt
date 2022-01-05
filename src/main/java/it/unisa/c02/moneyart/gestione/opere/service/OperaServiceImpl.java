package it.unisa.c02.moneyart.gestione.opere.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.model.dao.interfaces.UtenteDao;
import it.unisa.c02.moneyart.utils.production.Retriever;
import java.util.List;

/**
 * Questa classe implementa i metodi dell'interfaccia OperaService.
 */
public class OperaServiceImpl implements OperaService {

  /**
   * Costruttore senza paramentri.
   */
  public OperaServiceImpl() {
    this.operaDao = Retriever.getIstance(OperaDao.class);
    this.utenteDao = Retriever.getIstance(UtenteDao.class);
  }

  /**
   * Costruttore della classe.
   *
   * @param opera dao di un'opera
   * @param utente dao di un utente
   */
  public OperaServiceImpl(OperaDao opera, UtenteDao utente) {
    this.operaDao = opera;
    this.utenteDao = utente;
  }

  /**
   * Inserisce una nuova opera nel database.
   *
   * @param opera bean dell'opera da inserire
   * @return true se l'inserimento va a buon fine, false altrimenti
   */
  @Override
  public boolean addArtwork(Opera opera) {
    if (checkOpera(opera) == true
          && checkArtwork(opera.getArtista().getId(), opera.getNome()) == true) {
      operaDao.doCreate(opera);
      return true;
    } else {
      return false;
    }

  }

  /**
   * Controlla l'univocità del nome di un'opera presente nel db.
   *
   * @param id dell'utente creatore
   * @param name titolo dell'opera
   * @return true se esiste un'opera con quel nome, false altrimenti
   */
  @Override
  public boolean checkArtwork(int id, String name) {
    List<Opera> opere = operaDao.doRetrieveAllByArtistId(id);

    if (opere != null) {
      for (Opera o : opere) {
        if (o.getNome().equals(name)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Restituisce un bean opera.
   *
   * @param id dell'opera
   * @return del bean opera se presente nel database, null altrimenti
   */
  @Override
  public Opera getArtwork(int id) {
    Opera opera = operaDao.doRetrieveById(id);

    if (opera == null) {
      return null;
    } else {
      return opera;
    }
  }

  /**
   * Restituisce l'opera cercata nel database.
   *
   * @param name dell'opera da cercare
   * @return una lista di opere avente il nome dell'argomento, null altrimenti
   */
  @Override
  public List<Opera> searchOpera(String name) {

    List<Opera> opere = operaDao.doRetrieveAllByName(name);

    if(opere != null) {
      return opere;
    } else {
      return null;
    }
  }

  /**
   * Restituisce tutte le opere di un artista.
   *
   * @param id dell'artista
   * @return una lista di opere
   */
  @Override
  public List<Opera> getArtworkByUser(int id) {
    List<Opera> opere = operaDao.doRetrieveAllByArtistId(id);

    if (opere != null) {
      return opere;
    } else {
      return null;
    }
  }

  private boolean checkOpera(Opera opera) {
    if (opera.getNome() != null && opera.getImmagine() != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Variabili d'istanza.
   */
  private OperaDao operaDao;
  private UtenteDao utenteDao;
}
