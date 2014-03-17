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
 * Home object for domain model class FitsFileCut.
 * @see com.gwac.model.FitsFileCut
 * @author Hibernate Tools
 */
public class FitsFileCutHome {

    private static final Log log = LogFactory.getLog(FitsFileCutHome.class);

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
    
    public void persist(FitsFileCut transientInstance) {
        log.debug("persisting FitsFileCut instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(FitsFileCut instance) {
        log.debug("attaching dirty FitsFileCut instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(FitsFileCut instance) {
        log.debug("attaching clean FitsFileCut instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(FitsFileCut persistentInstance) {
        log.debug("deleting FitsFileCut instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public FitsFileCut merge(FitsFileCut detachedInstance) {
        log.debug("merging FitsFileCut instance");
        try {
            FitsFileCut result = (FitsFileCut) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public FitsFileCut findById( long id) {
        log.debug("getting FitsFileCut instance with id: " + id);
        try {
            FitsFileCut instance = (FitsFileCut) sessionFactory.getCurrentSession()
                    .get("com.gwac.model.FitsFileCut", id);
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
    
    public List findByExample(FitsFileCut instance) {
        log.debug("finding FitsFileCut instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.gwac.model.FitsFileCut")
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

