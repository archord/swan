package com.gwac.dao;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class MysqlHibernateDaoImpl<T extends Serializable> implements BaseHibernateDao<T> {

  private static final Log log = LogFactory.getLog(MysqlHibernateDaoImpl.class);
  public static final int SORT_ASC = 1;
  public static final int SORT_DESC = 2;
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
        log.debug("curSession is null!");
        return null;
      } else {
        List<T> list = curSession.createCriteria(clazz).list();
        return list;
      }
    } catch (HibernateException ex) {
      log.debug(ex.toString());
    }
    return null;
  }

  /**
   * 获取从start开始的resultSize个记录，默认按升序排序，如果orderNames和sort长 度不同，则忽略sort，按默认按升序排序。
   *
   * @param start 返回的第一个结果在数据库中的位置
   * @param resultSize 最多返回的记录条数
   * @param orderNames 需要排序的属性名称
   * @param sort 1 按照升序排序，2按照降序排序
   * @return 结果链表 Query q = session.createQuery("from FooBar as f");
   * q.setFirstResult(500); q.setMaxResults(100);
   */
  @SuppressWarnings("unchecked")
  public List<T> findRecord(int start, int resultSize, String[] orderNames, int[] sort) {
    Criteria crt = getCurrentSession().createCriteria(clazz);
    crt.setFirstResult(start);
    crt.setMaxResults(resultSize);
    if (orderNames != null && sort != null) {
      if (orderNames.length == sort.length) {
        for (int i = 0; i < orderNames.length; i++) {
          if (sort[i] == SORT_ASC) {
            crt.addOrder(Order.asc(orderNames[i]));
          } else {
            crt.addOrder(Order.desc(orderNames[i]));
          }
        }
      } else {
        for (String ord : orderNames) {
          crt.addOrder(Order.asc(ord));
        }
      }
    }
    return crt.list();
  }

  @Transactional(readOnly = false)
  public void save(final T entity) {
    getCurrentSession().persist(entity);
  }

  @Transactional(readOnly = false)
  public void save(final List<T> entList) {
    getCurrentSession().saveOrUpdate(entList);
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
