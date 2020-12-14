package storage.postgresql;

import domain.Lesson;
import storage.LessonRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
				Integer id = lessonsSet.getInt(0);
				LocalDate localDate = lessonsSet.getObject(1, LocalDate.class);
				LocalTime localTime = lessonsSet.getObject(2, LocalTime.class);

				LocalDateTime dateTime = localDate.atTime(localTime);

				String homework = lessonsSet.getString(3);
				String discipline = lessonsSet.getString(4);
				String description = lessonsSet.getString(6);
				String teacherLogin = lessonsSet.getString(7);

				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(1));
				}

				lesson.setLessonId(id);
				lesson.setDateTime(dateTime);
				lesson.setDescription(description);
				lesson.setHomework(homework);
				lesson.setDiscipline(discipline);
				lesson.setTeacherLogin(teacherLogin);
				lesson.setGroupId(Integer.valueOf(groupId));

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
				Integer id = lessonsSet.getInt(0);
				LocalDate localDate = lessonsSet.getObject(1, LocalDate.class);
				LocalTime localTime = lessonsSet.getObject(2, LocalTime.class);

				LocalDateTime dateTime = localDate.atTime(localTime);

				String homework = lessonsSet.getString(3);
				String discipline = lessonsSet.getString(4);
				Integer groupId = lessonsSet.getInt(5);
				String description = lessonsSet.getString(6);


				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(1));
				}

				lesson.setLessonId(id);
				lesson.setDateTime(dateTime);
				lesson.setDescription(description);
				lesson.setHomework(homework);
				lesson.setDiscipline(discipline);
				lesson.setTeacherLogin(teacherLogin);
				lesson.setGroupId(groupId);

				lessons.add(lesson);
			}

			lessonsSet.close();

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
				Lesson lesson = new Lesson(presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(1));
				}

				lesson.setLessonId(id);
				lesson.setDateTime(dateTime);
				lesson.setDescription(description);
				lesson.setHomework(homework);
				lesson.setDiscipline(discipline);
				lesson.setTeacherLogin(teacherLogin);
				lesson.setGroupId(groupId);

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
				Integer id = lessonsSet.getInt(0);
				LocalDate localDate = lessonsSet.getObject(1, LocalDate.class);
				LocalTime localTime = lessonsSet.getObject(2, LocalTime.class);

				LocalDateTime dateTime = localDate.atTime(localTime);

				String homework = lessonsSet.getString(3);
				String discipline = lessonsSet.getString(4);
				Integer groupId = lessonsSet.getInt(5);
				String description = lessonsSet.getString(6);
				String teacherLogin = lessonsSet.getString(7);

				Map<String, Boolean> presentsForLesson = new HashMap<>();
				Lesson lesson = new Lesson(presentsForLesson);

				String getPresentsCommand = String.format("Select * FROM Presents WHERE lesson_id = %s", id);

				ResultSet presentSet = connector.executeStatement(getPresentsCommand);
				while (presentSet.next()) {
					lesson.addPresent(presentSet.getString(1));
				}

				lesson.setLessonId(id);
				lesson.setDateTime(dateTime);
				lesson.setDescription(description);
				lesson.setHomework(homework);
				lesson.setDiscipline(discipline);
				lesson.setTeacherLogin(teacherLogin);
				lesson.setGroupId(groupId);

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
		String deleteLessonCommand = String.format("DELETE FROM Lessons WHERE id = %s", id);

		ResultSet resultSet = connector.executeStatement(deleteLessonCommand);
		try {
			resultSet.close();
		} catch (SQLException ignored) {}
	}

	@Override
	public void update(Lesson entity) {
		try {
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
			resultSet.close();

			Map<String, Boolean> presents = entity.getIsPresent();

			for (String name : presents.keySet()) {
				if (presents.get(name)) {
					String existsPresentCommand = String.format("EXISTS (SELECT * FROM Presents WHERE lesson_id = %s AND login = '%s')", id, name);

					ResultSet present = connector.executeStatement(existsPresentCommand);
					if (present == null) {
						String addPresentCommand = String.format("INSERT INTO Presents VALUES(%s, '%s')", id, name);

						ResultSet results = connector.executeStatement(addPresentCommand);
						results.close();
					} else {
						present.close();
					}
				} else {
					String existsPresentCommand = String.format("EXISTS (SELECT * FROM Presents WHERE lesson_id = %s AND login = '%s')", id, name);

					ResultSet present = connector.executeStatement(existsPresentCommand);
					if (present != null) {
						present.close();

						String deletePresentCommand = String.format("DELETE FROM Presents WHERE lesson_id = %s AND login = '%s'", id, name);

						ResultSet results = connector.executeStatement(deletePresentCommand);
						results.close();
					}
				}
			}
		} catch (SQLException ignored) {}
	}

	@Override
	public boolean existsById(Integer login) {
		String existsLessonCommand = String.format("EXISTS (SELECT discipline FROM Lessons WHERE id = %s)", login);

		ResultSet lesson = connector.executeStatement(existsLessonCommand);
		return lesson != null;
	}
}