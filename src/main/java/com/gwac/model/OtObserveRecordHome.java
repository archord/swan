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
 * Home object for domain model class OtObserveRecord.
 * @see com.gwac.model.OtObserveRecord
 * @author Hibernate Tools
 */
public class OtObserveRecordHome {

    private static final Log log = LogFactory.getLog(OtObserveRecordHome.class);

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
    
    public void persist(OtObserveRecord transientInstance) {
        log.debug("persisting OtObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(OtObserveRecord instance) {
        log.debug("attaching dirty OtObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(OtObserveRecord instance) {
        log.debug("attaching clean OtObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(OtObserveRecord persistentInstance) {
        log.debug("deleting OtObserveRecord instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public OtObserveRecord merge(OtObserveRecord detachedInstance) {
        log.debug("merging OtObserveRecord instance");
        try {
            OtObserveRecord result = (OtObserveRecord) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public OtObserveRecord findById( long id) {
        log.debug("getting OtObserveRecord instance with id: " + id);
        try {
            OtObserveRecord instance = (OtObserveRecord) sessionFactory.getCurrentSession()
                    .get("com.gwac.model.OtObserveRecord", id);
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
    
    public List findByExample(OtObserveRecord instance) {
        log.debug("finding OtObserveRecord instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.gwac.model.OtObserveRecord")
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

