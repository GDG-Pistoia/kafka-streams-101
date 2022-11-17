package it.gdg.pt.control;

import it.gdg.pt.control.api.HasPersistence;
import it.gdg.pt.control.api.HasSaveOrUpdate;
import it.gdg.pt.entities.LogarithmicReturn;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class LogarithmicReturnControl implements HasSaveOrUpdate<LogarithmicReturn> {

    @Inject
    EntityManager entityManager;

    @Override
    public EntityManager getEm() {
        return entityManager;
    }

    @Override
    public Class<LogarithmicReturn> getEntityType() {
        return LogarithmicReturn.class;
    }
}
