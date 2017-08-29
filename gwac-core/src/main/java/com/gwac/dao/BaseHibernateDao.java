package com.gwac.dao;

import java.util.List;

public interface BaseHibernateDao<T> {

//  public void setClazz(final Class<T> clazzToSet);

  public Number count();

  public T getById(final Long id);

  public List<T> findAll();

  public List<T> findRecord(int start, int resultSize, String[] orderNames, int[] sort);

  public void save(final T entity);

  public void save(final List<T> entList);

  public void update(final T entity);

  public void delete(final T entity);

  public void deleteById(final Long entityId);
  
  public void deleteAll(String tableName);
}
