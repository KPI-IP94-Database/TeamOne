package storage.postgresql;

import domain.Lesson;
import domain.User;
import domain.UserType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import storage.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

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
	public void saveNewEntity() {
		User user1 = new User("login1", "12345", "Lasfasc", UserType.STUDENT, 1);
		User user2 = new User("login2", "12345", "Aafasdc", UserType.TEACHER, null);
		User user3 = new User("login1", "12345", "Lasfasc", UserType.STUDENT, 1);
		User saveNewUser1 = repository.saveNewEntity(user1);
		User saveNewUser2 = repository.saveNewEntity(user2);
		User saveNewUser3 = repository.saveNewEntity(user3);
		assertEquals(saveNewUser1, user1);
		assertEquals(saveNewUser2, user2);
		assertEquals(saveNewUser3, user3);
	}

	@Test
	public void findById() {
		Optional<User> user1 = repository.findById("wqa092");
		assertTrue(user1.isPresent());

		Optional<User> user2 = repository.findById("julius44");
		assertTrue(user2.isPresent());
	}

	@Test
	public void findAll() {
		ArrayList<User> findAll = (ArrayList<User>) repository.findAll();
		ArrayList<User> userList = new ArrayList<>();
		userList.add(new User("vasya092", "dssdfds1124", "Ivaniuk V.O.", UserType.GROUP_HEAD, 1));
		userList.add(new User("dominar3000", "d23rfw124", "Naluvayko R.I.", UserType.STUDENT, 1));
		userList.add(new User("wqa092", "sdfhsdfdsf124", "Pokolyuk W.K.", UserType.STUDENT, 1));
		userList.add(new User("eeesya092", "ds34tertd4", "Ivanko K.O.", UserType.STUDENT, 1));

		userList.add(new User("sssya092", "dssdfdffs1124", "Ivator V.O.", UserType.GROUP_HEAD, 2));
		userList.add(new User("dominat00", "d2ff3rfw124", "Nalutol R.I.", UserType.STUDENT, 2));
		userList.add(new User("wireas34", "sdfhsdsdfdsf124", "Zelensky P.P.", UserType.STUDENT, 2));
		userList.add(new User("va9rkw2", "dssdfdertd4", "Ilonov K.E.", UserType.STUDENT, 2));

		userList.add(new User("fsf1467", "dssdfds1124", "Josepe S.A.", UserType.GROUP_HEAD, 3));
		userList.add(new User("nsddw002", "d23rfw124", "Naluvayko R.I.", UserType.STUDENT, 3));
		userList.add(new User("port92", "sdfhsdfdsf124", "Pokolyuk W.K.", UserType.STUDENT, 3));
		userList.add(new User("kolya073", "ds34tertd4", "Ivanko K.O.", UserType.STUDENT, 3));

		userList.add(new User("upa1221", "kwev3es", "Bandera S.A.", UserType.TEACHER, null));
		userList.add(new User("directoria1", "f;owejg89", "Petliura S.V.", UserType.TEACHER, null));

		userList.add(new User("julius44", "ADAsdas!3!@!#asd", "Julius Caesar", UserType.ADMIN, null));
		userList.add(new User("napoleon123", "123SA@q231", "Napoleon Bonaparte", UserType.ADMIN, null));

		userList.add(new User("login1", "12345", "Lasfasc", UserType.STUDENT, 1));
		userList.add(new User("login2", "12345", "Aafasdc", UserType.TEACHER, null));
		userList.add(new User("login1", "12345", "Lasfasc", UserType.STUDENT, 1));

		assertEquals(findAll, userList);
	}

	@Test
	public void deleteById() {
		try {
			String userLogin = "vasya092";
			repository.deleteById(userLogin);
			ResultSet resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", userLogin));
			assertFalse(resultSet.next());

			userLogin = "dominat00";
			repository.deleteById(userLogin);
			resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", userLogin));
			assertFalse(resultSet.next());

			userLogin = "port92";
			repository.deleteById(userLogin);
			resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", userLogin));
			assertFalse(resultSet.next());

			userLogin = "adf122ewds";
			repository.deleteById(userLogin);
			resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", userLogin));
			assertFalse(resultSet.next());

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
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
