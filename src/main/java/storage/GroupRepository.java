package storage;

import domain.Group;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Integer> {
    Optional<Group> findGroupByName(String name);
}
