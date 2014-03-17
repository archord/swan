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
 * Home object for domain model class StarListFile.
 * @see com.gwac.model.StarListFile
 * @author Hibernate Tools
 */
public class StarListFileHome {

    private static final Log log = LogFactory.getLog(StarListFileHome.class);

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
    
    public void persist(StarListFile transientInstance) {
        log.debug("persisting StarListFile instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(StarListFile instance) {
        log.debug("attaching dirty StarListFile instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(StarListFile instance) {
        log.debug("attaching clean StarListFile instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(StarListFile persistentInstance) {
        log.debug("deleting StarListFile instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public StarListFile merge(StarListFile detachedInstance) {
        log.debug("merging StarListFile instance");
        try {
            StarListFile result = (StarListFile) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public StarListFile findById( long id) {
        log.debug("getting StarListFile instance with id: " + id);
        try {
            StarListFile instance = (StarListFile) sessionFactory.getCurrentSession()
                    .get("com.gwac.model.StarListFile", id);
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
    
    public List findByExample(StarListFile instance) {
        log.debug("finding StarListFile instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.gwac.model.StarListFile")
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

