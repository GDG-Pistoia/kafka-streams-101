package it.gdg.pt.control;

import it.gdg.pt.control.api.HasPersistence;
import it.gdg.pt.control.api.HasSaveOrUpdate;
import it.gdg.pt.entities.LogarithmicReturn;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Slf4j
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

    @Override
    public LogarithmicReturn saveOrUpdate(LogarithmicReturn entity) {
        entity.setInserted_at(LocalDateTime.now());
        return doSaveOrUpdate(entity);
    }
}
