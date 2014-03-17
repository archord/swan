package com.gwac.model;
// Generated 2014-3-2 18:54:13 by Hibernate Tools 3.2.2.GA


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class StarKnowObserveRecord.
 * @see com.gwac.model.StarKnowObserveRecord
 * @author Hibernate Tools
 */
public class StarKnowObserveRecordHome {

    private static final Log log = LogFactory.getLog(StarKnowObserveRecordHome.class);

    private final SessionFactory sessionFactory = getSessionFactory();
    
    protected SessionFactory getSessionFactory() {
        try {
            return (SessionFactory) new InitialContext().lookup("SessionFactory");
        }
        catch (Exception e) {
            log.error("Could not locate SessionFactory in JNDI", e);
            throw new IllegalStateException("Could not locate SessionFactory in JNDI");
        }
    }
    
    public void persist(StarKnowObserveRecord transientInstance) {
        log.debug("persisting StarKnowObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(StarKnowObserveRecord instance) {
        log.debug("attaching dirty StarKnowObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(StarKnowObserveRecord instance) {
        log.debug("attaching clean StarKnowObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(StarKnowObserveRecord persistentInstance) {
        log.debug("deleting StarKnowObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public StarKnowObserveRecord merge(StarKnowObserveRecord detachedInstance) {
        log.debug("merging StarKnowObserveRecord instance");
        try {
            StarKnowObserveRecord result = (StarKnowObserveRecord) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public StarKnowObserveRecord findById( long id) {
        log.debug("getting StarKnowObserveRecord instance with id: " + id);
        try {
            StarKnowObserveRecord instance = (StarKnowObserveRecord) sessionFactory.getCurrentSession()
                    .get("com.gwac.model.StarKnowObserveRecord", id);
            if (instance==null) {
                log.debug("get successful, no instance found");
            }
            else {
                log.debug("get successful, instance found");
            }
            return instance;
        }
        catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(StarKnowObserveRecord instance) {
        log.debug("finding StarKnowObserveRecord instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.gwac.model.StarKnowObserveRecord")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        }
        catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    } 
}

