package storage.postgresql;

import domain.Group;
import lombok.AllArgsConstructor;
import storage.GroupRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@AllArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {

    private Connector connector;

    @Override
    public Group saveNewEntity(Group entity) {
        String name = entity.getName();
        String groupheadLogin = entity.getGroupHeadLogin();
        String statement = "INSERT INTO Groups(name, grouphead_login) " +
                "VALUES('" + name + "', '" + groupheadLogin + "')";
        ResultSet result = connector.executeStatement(statement);
        try {
            Integer id = result.getInt(1);
            entity.setId(id);
            return entity;
        } catch (SQLException exception) {
            return null;
        }
    }

    @Override
    public Optional<Group> findById(Integer id) {
        String statement = "SELECT * FROM Groups WHERE id = " + id;
        ResultSet result = connector.executeStatement(statement);
        if (result == null) {
            return Optional.empty();
        } else {
            try {
                String name = result.getString(2);
                String groupHeadLogin = result.getString(3);
                return Optional.of(new Group(id, name, groupHeadLogin));
            } catch (SQLException e) {
                return Optional.empty();
            }
        }
    }

    @Override
    public Iterable<Group> findAll() {
        String statement = "SELECT * FROM Groups";
        ResultSet result = connector.executeStatement(statement);
        ArrayList<Group> groupList = new ArrayList<>();
        try {
            while (result.next()) {
                Integer id = result.getInt(1);
                String name = result.getString(2);
                String groupHeadLogin = result.getString(3);
                Group group = new Group(id, name, groupHeadLogin);
                groupList.add(group);
            }
        } catch (SQLException exception) {
            return null;
        }
        return groupList;
    }

    @Override
    public void deleteById(Integer id) {
        String statement = "DELETE FROM Groups WHERE id = " + id;
        connector.executeStatement(statement);
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
