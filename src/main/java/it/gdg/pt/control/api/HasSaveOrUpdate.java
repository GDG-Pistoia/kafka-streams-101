package it.gdg.pt.control.api;


import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceUnitUtil;
import javax.ws.rs.WebApplicationException;

import static javax.ws.rs.core.Response.Status.CONFLICT;

public interface HasSaveOrUpdate<T> extends HasPersistence<T> {

    default T saveOrUpdate(T entity){
        return doSaveOrUpdate(entity);
    };

    default T doSaveOrUpdate(T entity) {

        EntityManager em = getEm();
        PersistenceUnitUtil persistenceUnitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
        Object id = persistenceUnitUtil.getIdentifier(entity);

        if (id == null) {
            em.persist(entity);
        } else {
            try {
                entity = em.merge(entity);
                em.flush();
            } catch (OptimisticLockException e) {
                String msg = String.format(Constants.OPT_LOCK, entity.getClass().getSimpleName(), id);
                throw new WebApplicationException(msg, CONFLICT);
            }
        }

        return entity;
    }
}