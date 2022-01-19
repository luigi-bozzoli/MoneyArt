package it.unisa.c02.moneyart.gestione.opere.service;

import it.unisa.c02.moneyart.model.beans.Opera;
import it.unisa.c02.moneyart.model.blockchain.MoneyArtNft;
import it.unisa.c02.moneyart.model.dao.interfaces.OperaDao;
import it.unisa.c02.moneyart.utils.production.Sing;
import java.util.List;
import javax.inject.Inject;

/**
 * Questa classe implementa i metodi dell'interfaccia OperaService.
 */
public class OperaServiceImpl implements OperaService {

  /**
   * Costruttore senza paramentri.
   */
  public OperaServiceImpl() {
  }

  /**
   * Costruttore della classe.
   *
   * @param opera dao di un'opera
   */
  public OperaServiceImpl(OperaDao opera, MoneyArtNft moneyArtNft) {
    this.operaDao = opera;
    this.moneyArtNft = moneyArtNft;
  }

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
  @Override
  public boolean addArtwork(Opera opera) throws Exception {
    if (checkOpera(opera)
        && !checkArtwork(opera.getArtista().getId(), opera.getNome())) {
      moneyArtNft.create(opera.getId() + "").send();
      operaDao.doCreate(opera);

      return true;
    } else {
      return false;
    }

  }

  /**
   * Controlla l'univocità del nome di un'opera, per un determinato artista, presente nel db.
   *
   * @param id   dell'utente artista
   * @param name titolo dell'opera
   * @return true se esiste un'opera con quel nome,
   *         false altrimenti
   * @pre Utente.allIstances() -> exists(u:Utente | id = u.getId()) AND name <> null
   */
  @Override
  public boolean checkArtwork(int id, String name) {
    if(name == null){
      throw new IllegalArgumentException("Name is null");
    }
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
    return operaDao.doRetrieveById(id);
  }

  /**
   * Restituisce l'opera cercata nel database.
   *
   * @param name dell'opera da cercare
   * @return una lista di opere avente il nome dell'argomento, null altrimenti
   */
  @Override
  public List<Opera> searchOpera(String name) {
    return operaDao.doRetrieveAllByName(name);
  }

  /**
   * Restituisce tutte le opere di un artista.
   *
   * @param id dell'artista
   * @return una lista di opere
   */
  @Override
  public List<Opera> getArtworkByUser(int id) {
    return operaDao.doRetrieveAllByArtistId(id);
  }

  private boolean checkOpera(Opera opera) {
    return opera.getNome() != null && opera.getImmagine() != null;
  }

  /**
   * Variabili d'istanza.
   */
  @Inject
  private OperaDao operaDao;
  @Inject
  @Sing
  private MoneyArtNft moneyArtNft;
}
