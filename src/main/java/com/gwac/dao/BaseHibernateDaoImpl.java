package com.gwac.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;

@Transactional
public abstract class BaseHibernateDaoImpl<T extends Serializable> implements BaseHibernateDao<T> {

  private Class<T> clazz;
  SessionFactory sessionFactory;

  public void setClazz(final Class<T> clazzToSet) {
    clazz = clazzToSet;
  }

  public Number count() {
    return (Number) getCurrentSession().createCriteria(clazz).setProjection(Projections.rowCount()).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public T getById(final Long id) {
    if (id != null) {
      return (T) getCurrentSession().get(clazz, id);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    try {
      Session curSession = getCurrentSession();
      if (curSession == null) {
        System.out.println("curSession is null!");
        return null;
      } else {
        List<T> list = curSession.createCriteria(clazz).list();
        return list;
      }
    } catch (HibernateException ex) {
      System.out.println(ex.toString());
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll(int start, int resultSize) {
    Session curSession = getCurrentSession();
    return curSession.createCriteria(clazz)
            .setFirstResult(start)
            .setMaxResults(resultSize)
            .list();
  }

  @Transactional(readOnly = false)
  public void save(final T entity) throws Exception {
    getCurrentSession().persist(entity);
  }

  @Transactional(readOnly = false)
  public void update(final T entity) {
    getCurrentSession().merge(entity);
  }

  @Transactional(readOnly = false)
  public void delete(final T entity) {
    getCurrentSession().delete(entity);
  }

  @Transactional(readOnly = false)
  public void deleteById(final Long entityId) {
    this.delete(this.getById(entityId));
  }

  public final Session getCurrentSession() {
    return sessionFactory.getCurrentSession();
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
}
