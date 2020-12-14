package storage;

import java.util.Optional;

public interface CrudRepository<T, K> {

    /**
     * Must generate right id (even if entity has own id),
     * save it in database like new entity and return it with right id.
     *
     * USE ONLY FOR SAVING NEW ENTITY
     */
    T saveNewEntity(T entity);

    Optional<T> findById(K id);

    Iterable<T> findAll();

    void deleteById(K id);

    /**
     * USE ONLY FOR UPDATING ALREADY EXISTING ENTITY!
     */
    void update(T entity);

    boolean existsById(K login);
}
