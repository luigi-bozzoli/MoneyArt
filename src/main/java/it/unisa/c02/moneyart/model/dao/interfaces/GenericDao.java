package it.unisa.c02.moneyart.model.dao.interfaces;

import java.util.List;

/**
 * Questa classe rappresenta un'interfaccia generica contenente
 * i metodi in comnune per tutti i DAOs.
 *
 * @param <T> il tipo del DAO
 */
public interface GenericDao<T> {

  void doCreate(T item);

  T doRetrieveById(int id);

  List<T> doRetrieveAll(String filter);

  void doUpdate(T item);

  void doDelete(T item);
}
