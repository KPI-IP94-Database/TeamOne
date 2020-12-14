package storage.postgresql;

import domain.User;
import domain.UserType;
import lombok.AllArgsConstructor;
import storage.UserRepository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Optional;


@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private Connector connector;
    private final HashMap<UserType, String> convertUserTypeForm = init();

    @Override
    public User saveNewEntity(User entity) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void update(User entity) {
        String newUserType = convertUserTypeForm.get(entity.getUserType());
        String statement = "UPDATE Users SET " + "(name, group_id, user_type, password_salt, password_hash) = " +
                "('" + entity.getName() + "', " + entity.getGroupId() + ", '" + newUserType +
                "', '" + entity.getPassword() + "', '" + entity.getPassword() + "') " +
                "WHERE login = '" + entity.getLogin() + "'";
        connector.executeStatement(statement);
    }

    @Override
    public boolean existsById(String login) {
        String statement = "SELECT * FROM Users WHERE login = '" + login + "'";
        ResultSet result = connector.executeStatement(statement);
        return result != null;
    }

    private HashMap<UserType, String> init() {
        HashMap<UserType, String> convertHashMap = new HashMap<>();
        convertHashMap.put(UserType.STUDENT, "student");
        convertHashMap.put(UserType.GROUP_HEAD, "group_head");
        convertHashMap.put(UserType.TEACHER, "teacher");
        convertHashMap.put(UserType.ADMIN, "admin");
        return convertHashMap;
    }
}
