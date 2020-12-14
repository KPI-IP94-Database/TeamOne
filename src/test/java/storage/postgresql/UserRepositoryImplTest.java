package storage.postgresql;

import domain.User;
import domain.UserType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import storage.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserRepositoryImplTest {

    private UserRepository repository;
    private Connector connector;

    @Before
    public void setUp() {
        Connector.Builder builder = new Connector.Builder();
        builder.setUrl("jdbc:postgresql://localhost:5432/experiment").setUser("postgres").setPassword("root");

        connector = new Connector(builder);
        connector.getConnection();
        repository = new UserRepositoryImpl(connector);
    }

    @Test
    public void existsById() {
        boolean isGroupExist1 = repository.existsById("vasya092");
        boolean isGroupExist2 = repository.existsById("sssya092");
        boolean isGroupExist3 = repository.existsById("directoria1");

        assertTrue(isGroupExist1);
        assertTrue(isGroupExist2);
        assertTrue(isGroupExist3);

        boolean isGroupExist4 = repository.existsById("NotExitt");
        boolean isGroupExist5 = repository.existsById("Popopo");
        boolean isGroupExist6 = repository.existsById("Lalalal");

        assertFalse(isGroupExist4);
        assertFalse(isGroupExist5);
        assertFalse(isGroupExist6);
    }

    @Test
    public void update() throws SQLException {
        User user1 = new User("fsf1467", "123sacQW@asd", "Josepe S.A.", UserType.GROUP_HEAD, 3);
        repository.update(user1);
        String statement1 = "SELECT * FROM Groups WHERE name = 'Josepe S.A.'";
        ResultSet result1 = connector.executeStatement(statement1);
        while (result1.next()) {
            assertEquals(result1.getString("login"), "fsf1467");
            assertEquals(result1.getString("name"), "Josepe S.A.");
            assertEquals(result1.getInt("group_id"), 3);
            assertEquals(result1.getString("user_type"), "group_head");
            assertEquals(result1.getString("password_hash"), "123sacQW@asd");
            assertEquals(result1.getString("password_salt"), "123sacQW@asd");
        }

        User user2 = new User("julius44", "12QW@asd", "NoName", UserType.STUDENT, null);
        repository.update(user1);
        String statement2 = "SELECT * FROM Groups WHERE name = 'NoName'";
        ResultSet result2 = connector.executeStatement(statement2);
        while (result2.next()) {
            assertEquals(result2.getString("login"), "julius44");
            assertEquals(result2.getString("name"), "NoName");
            assertEquals(result2.getInt("group_id"), "NULL");
            assertEquals(result2.getString("user_type"), "student");
            assertEquals(result2.getString("password_hash"), "12QW@asd");
            assertEquals(result2.getString("password_salt"), "12QW@asd");
        }
    }

    @After
    public void setDown() {
        String statement1 = "UPDATE Users SET (login, name, group_id, user_type, password_salt, password_hash)" +
                " VALUES('fsf1467', 'Josepe S.A.', 3, 'group_head', '123sacQW@asd', '123456789');";
        String statement2 = "UPDATE Users SET (login, name, user_type, password_salt, password_hash)" +
                " VALUES('julius44', 'Julius Caesar', 'admin', 'rome3000', 'ADAsdas!3!@!#asd');";
        connector.executeStatement(statement1);
        connector.executeStatement(statement2);
    }
}
