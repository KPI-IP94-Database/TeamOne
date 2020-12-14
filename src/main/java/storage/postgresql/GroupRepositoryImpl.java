package storage.postgresql;

import domain.Group;
import lombok.AllArgsConstructor;
import storage.GroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private Connector connector;

    @Override
    public Group saveNewEntity(Group entity) {
        return null;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Iterable<Group> findAll() {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Group entity) {
        String statement = "UPDATE Groups SET " + "(name, grouphead_login) = " +
                "('" + entity.getName() + "', '" + entity.getGroupHeadLogin()+"') " +
                "WHERE id = " + entity.getId();
        connector.executeStatement(statement);
    }

    @Override
    public boolean existsById(Integer id) {
        String statement = "SELECT * FROM Groups WHERE id = " + id;
        ResultSet result = connector.executeStatement(statement);
        return result != null;
    }

    @Override
    public Optional<Group> findGroupByName(String name) {
        String statement = "SELECT * FROM Groups WHERE name = '"
                + name + "'";
        ResultSet result = connector.executeStatement(statement);
        if (result == null) {
            return Optional.empty();
        } else {
            try {
                Integer id = result.getInt(1);
                String groupHeadLogin = result.getString(3);
                return Optional.of(new Group(id, name, groupHeadLogin));
            } catch (SQLException e) {
                return Optional.empty();
            }
        }
    }
}
