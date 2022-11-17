package it.gdg.pt.control.api;

import javax.persistence.EntityManager;

public interface HasPersistence<T> {
    EntityManager getEm();
    Class<T> getEntityType();
}
