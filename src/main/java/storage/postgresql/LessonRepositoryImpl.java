package storage.postgresql;

import domain.Lesson;
import storage.LessonRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LessonRepositoryImpl implements LessonRepository {
	private Connector connector;

	public LessonRepositoryImpl(Connector connector) {
		this.connector = connector;
	}

	@Override
	public List<Lesson> findLessonsByGroupIdFromInterval(String groupId, LocalDate from, LocalDate to) {
		String getLessonsCommand = String.format("SELECT * FROM Lessons WHERE date >= '%s' AND date < '%s' AND group_id = %s",
				from, to, groupId);

		ResultSet lessonsSet = connector.executeStatement(getLessonsCommand);

		ArrayList<Lesson> lessons = new ArrayList<>();

		try {
			while (lessonsSet.next()) {
				Integer id = lessonsSet.getInt(1);
				String localDate = lessonsSet.getString(2);
				String localTime = lessonsSet.getString(3);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(localDate + " " + localTime, formatter);

				String homework = lessonsSet.getString(4);
				String discipline = lessonsSet.getString(5);
				String description = lessonsSet.getString(7);
				String teacherLogin = lessonsSet.getString(8);

				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(id, dateTime, description, discipline, homework, Integer.valueOf(groupId), teacherLogin, presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(2));
				}
				lessons.add(lesson);
			}

			lessonsSet.close();

			return lessons;
		} catch (SQLException ex) {
			return null;
		}
	}

	@Override
	public List<Lesson> findLessonsByTeacherIdFromInterval(String teacherLogin, LocalDate from, LocalDate to) {
		String getLessonsCommand = String.format("SELECT * FROM Lessons WHERE date >= '%s' AND date < '%s' AND teacher_login = '%s'",
				from, to, teacherLogin);

		ResultSet lessonsSet = connector.executeStatement(getLessonsCommand);

		ArrayList<Lesson> lessons = new ArrayList<>();

		try {
			while (lessonsSet.next()) {
				Integer id = lessonsSet.getInt(1);
				String localDate = lessonsSet.getString(2);
				String localTime = lessonsSet.getString(3);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(localDate + " " + localTime, formatter);

				String homework = lessonsSet.getString(4);
				String discipline = lessonsSet.getString(5);
				Integer groupId = lessonsSet.getInt(6);
				String description = lessonsSet.getString(7);


				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(id, dateTime, description, discipline, homework, groupId, teacherLogin, presentsForLesson);


				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(2));
				}

				lessons.add(lesson);
			}

			return lessons;
		} catch (SQLException ex) {
			return null;
		}
	}

	@Override
	public Lesson saveNewEntity(Lesson entity) {
		String statement = "INSERT INTO Lessons VALUES('" + entity.getLessonId() +
				"', '" + entity.getDateTime() + "', '" + entity.getDescription() +
				"', '" + entity.getHomework() + "', '" + entity.getDiscipline() +
				"', '" + entity.getTeacherLogin() + "', '" + entity.getGroupId() + "')";
		ResultSet result = connector.executeStatement(statement);
		try {
			Integer id = result.getInt(0);
			entity.setLessonId(id);
			return entity;
		} catch (SQLException exception) {
			return null;
		}
	}

	@Override
	public Optional<Lesson> findById(Integer id) {
		String statement = "SELECT * FROM Users WHERE login = '" + id + "')";
		ResultSet lessonsSet = connector.executeStatement(statement);
		if (lessonsSet != null) {
			try {
				LocalDate localDate = lessonsSet.getObject(1, LocalDate.class);
				LocalTime localTime = lessonsSet.getObject(2, LocalTime.class);

				LocalDateTime dateTime = localDate.atTime(localTime);

				String homework = lessonsSet.getString(3);
				String discipline = lessonsSet.getString(4);
				Integer groupId = lessonsSet.getInt(5);
				String description = lessonsSet.getString(6);
				String teacherLogin = lessonsSet.getString(7);

				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(id, dateTime, description, discipline, homework, groupId, teacherLogin, presentsForLesson);


				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(1));
				}

				return Optional.of(lesson);
			} catch (SQLException e) {
				return Optional.empty();
			}
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Lesson> findAll() {
		String getLessonsCommand = "SELECT * FROM Lessons";

		ResultSet lessonsSet = connector.executeStatement(getLessonsCommand);

		ArrayList<Lesson> lessons = new ArrayList<>();
		try {
			while (lessonsSet.next()) {
				Integer id = lessonsSet.getInt(1);
				String localDate = lessonsSet.getString(2);
				String localTime = lessonsSet.getString(3);

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(localDate + " " + localTime, formatter);

				String homework = lessonsSet.getString(4);
				String discipline = lessonsSet.getString(5);
				Integer groupId = lessonsSet.getInt(6);
				String description = lessonsSet.getString(7);
				String teacherLogin = lessonsSet.getString(8);

				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(id, dateTime, description, discipline, homework, groupId, teacherLogin, presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(2));
				}

				lessons.add(lesson);
			}

			lessonsSet.close();

			return lessons;
		} catch (SQLException ex) {
			return null;
		}
	}

	@Override
	public void deleteById(Integer id) {
		String deletePresentsCommand = String.format("DELETE FROM Presents WHERE lesson_id = %s", id);
		connector.executeStatement(deletePresentsCommand);

		String deleteLessonCommand = String.format("DELETE FROM Lessons WHERE id = %s", id);
		connector.executeStatement(deleteLessonCommand);

	}

	@Override
	public void update(Lesson entity) {
		LocalDate date = entity.getDateTime().toLocalDate();
		LocalTime time = entity.getDateTime().toLocalTime();

		Integer id = entity.getLessonId();
		String homework = entity.getHomework();
		String discipline = entity.getDiscipline();
		Integer groupId = entity.getGroupId();
		String description = entity.getDescription();
		String teacherLogin = entity.getTeacherLogin();

		String updateLessonCommand = String.format("UPDATE Lessons SET date = date '%s', time = time '%s', homework = '%s', discipline = '%s', group_id = %s, description = '%s', teacher_login = '%s' WHERE id = %s",
				date, time, homework, discipline, groupId, description, teacherLogin, id);

		ResultSet resultSet = connector.executeStatement(updateLessonCommand);

		Map<String, Boolean> presents = entity.getIsPresent();

		for (String name : presents.keySet()) {
			if (presents.get(name)) {
				String existsPresentCommand = String.format("SELECT * FROM Presents WHERE lesson_id = %s AND login = '%s'", id, name);

				ResultSet present = connector.executeStatement(existsPresentCommand);
				if (present == null) {
					String addPresentCommand = String.format("INSERT INTO Presents VALUES(%s, '%s')", id, name);

					connector.executeStatement(addPresentCommand);

				}
			} else {
				String existsPresentCommand = String.format("SELECT * FROM Presents WHERE lesson_id = %s AND login = '%s'", id, name);

				ResultSet present = connector.executeStatement(existsPresentCommand);
				if (present != null) {

					String deletePresentCommand = String.format("DELETE FROM Presents WHERE lesson_id = %s AND login = '%s'", id, name);

					connector.executeStatement(deletePresentCommand);
				}
			}
		}
	}

	@Override
	public boolean existsById(Integer login) {
		String existsLessonCommand = String.format("SELECT discipline FROM Lessons WHERE id = %s", login);

		ResultSet lesson = connector.executeStatement(existsLessonCommand);

        try {
        	return lesson.next();
		} catch (SQLException ex) {
        	return false;
		}
	}
}
