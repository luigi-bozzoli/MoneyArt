package it.unisa.c02.moneyart.model.dao.interfaces;

import java.util.List;

/**
 * Questa classe rappresenta un'interfaccia generica contenente
 * i metodi in comune per tutti i DAOs.
 *
 * @param <T> il tipo del DAO
 */
public interface GenericDao<T> {
  /**
   * Inserisce un item nel database.
   *
   * @param item l'oggetto da inserire nel database
   */
  boolean doCreate(T item);

  /**
   * Ricerca nel database un item tramite un identificativo unico.
   *
   * @param id l'identificativo dell'item
   * @return l'item trovato nel database
   */
  T doRetrieveById(int id);

  /**
   * Ricerca nel database tutti gli item, eventualmente ordinati
   * tramite un filtro.
   *
   * @param filter filtro di ordinamento delle tuple
   * @return la collezione di item trovata nel database
   */
  List<T> doRetrieveAll(String filter);

  /**
   * Aggiorna l'item nel database.
   *
   * @param item l'item da aggiornare
   */
  void doUpdate(T item);

  /**
   * Elimina l'item dal database.
   *
   * @param item l'item da eliminare
   */
  void doDelete(T item);
}
