package it.gdg.pt.control.api;

public interface HasFindById<T, K> extends HasPersistence<T> {

    T findById(K id);

    default T doFindById(K id) {
        return getEm().find(getEntityType(), id);

    }
}