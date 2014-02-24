package com.gwac.model;
// Generated Feb 14, 2014 3:32:17 PM by Hibernate Tools 3.2.2.GA


import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class DataProcessMachine.
 * @see com.gwac.model.DataProcessMachine
 * @author Hibernate Tools
 */
public class DataProcessMachineHome {

    private static final Log log = LogFactory.getLog(DataProcessMachineHome.class);

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
    
    public void persist(DataProcessMachine transientInstance) {
        log.debug("persisting DataProcessMachine instance");
        try {
            sessionFactory.getCurrentSession().persist(transientInstance);
            log.debug("persist successful");
        }
        catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }
    
    public void attachDirty(DataProcessMachine instance) {
        log.debug("attaching dirty DataProcessMachine instance");
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(DataProcessMachine instance) {
        log.debug("attaching clean DataProcessMachine instance");
        try {
            sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        }
        catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void delete(DataProcessMachine persistentInstance) {
        log.debug("deleting DataProcessMachine instance");
        try {
            sessionFactory.getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        }
        catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public DataProcessMachine merge(DataProcessMachine detachedInstance) {
        log.debug("merging DataProcessMachine instance");
        try {
            DataProcessMachine result = (DataProcessMachine) sessionFactory.getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        }
        catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }
    
    public DataProcessMachine findById( short id) {
        log.debug("getting DataProcessMachine instance with id: " + id);
        try {
            DataProcessMachine instance = (DataProcessMachine) sessionFactory.getCurrentSession()
                    .get("com.gwac.model.DataProcessMachine", id);
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
    
    public List findByExample(DataProcessMachine instance) {
        log.debug("finding DataProcessMachine instance by example");
        try {
            List results = sessionFactory.getCurrentSession()
                    .createCriteria("com.gwac.model.DataProcessMachine")
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

