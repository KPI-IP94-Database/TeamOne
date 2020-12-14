package storage.postgresql;

import domain.Lesson;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class LessonRepositoryImplTest {

    private Connector connector;
    private LessonRepositoryImpl repository;

    @Before
    public void setUp() {
        Connector.Builder builder = new Connector.Builder();
        builder.setUrl("jdbc:postgresql://localhost:5432/experiment").setUser("postgres").setPassword("root");

        connector = new Connector(builder);
        connector.getConnection();

        repository = new LessonRepositoryImpl(connector);

        connector.executeStatement("DROP TABLE IF EXISTS Lessons, Presents, Groups, Users");

        connector.executeStatement("CREATE TABLE Groups ( id bigserial PRIMARY KEY, name varchar(50) NOT NULL)");
        connector.executeStatement("CREATE TABLE Users (  login varchar(50) primary key,  name varchar(50) NOT NULL,  " +
                "group_id bigserial REFERENCES groups(id),  user_type user_type NOT NULL,   password_salt varchar(50) NOT NULL,   password_hash varchar(50) NOT NULL )");

        connector.executeStatement("ALTER TABLE Groups ADD grouphead_login varchar(50)");
        connector.executeStatement("ALTER TABLE Groups ADD CONSTRAINT fk_grouphead_login FOREIGN KEY (grouphead_login) REFERENCES Users(login)");

        connector.executeStatement("CREATE TABLE Lessons (  id bigserial PRIMARY KEY,  date date NOT NULL,  time time NOT NULL,  homework text,  discipline varchar(30) NOT NULL, " +
                "group_id bigserial NOT NULL REFERENCES Groups(id),  description text,  teacher_login varchar(50) NOT NULL REFERENCES Users(login) )");

        connector.executeStatement("CREATE TABLE Presents (  lesson_id bigserial REFERENCES Lessons(id),  login varchar(50) REFERENCES Users(login),  PRIMARY KEY(lesson_id, login) )");

        connector.executeStatement("INSERT INTO Groups(id, name) VALUES(1, 'First Group')");
        connector.executeStatement("INSERT INTO Groups(id, name) VALUES(2, 'Second Group')");
        connector.executeStatement("INSERT INTO Groups(id, name) VALUES(3, 'Third Group')");

        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash)" +
                "VALUES('vasya092', 'Ivaniuk V.O.', 1, 'group_head', 'sdf323fs!F$@2f', 'dssdfds1124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('dominar3000', 'Naluvayko R.I.', 1, 'student', 'ssdfd23fsd@2f', 'd23rfw124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('wqa092', 'Pokolyuk W.K.', 1, 'student', 'dsfhsfdh4', 'sdfhsdfdsf124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('eeesya092', 'Ivanko K.O.', 1, 'student', 'sdf34tgergf', 'ds34tertd4')");

        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('sssya092', 'Ivator V.O.', 2, 'group_head', 'sdf323fs!ht5F$@2f', 'dssdfdffs1124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('dominat00', 'Nalutol R.I.', 2, 'student', 'ssdfd23fs4yd@2f', 'd2ff3rfw124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('wireas34', 'Zelensky P.P.', 2, 'student', 'dsfhsfgfdh4', 'sdfhsdsdfdsf124')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('va9rkw2', 'Ilonov K.E.', 2, 'student', 'sdsdfsertgf', 'dssdfdertd4')");

        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('fsf1467', 'Josepe S.A.', 3, 'group_head', '123sacQW@asd', '123456789')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('nsddw002', 'Naluvayko R.I.', 3, 'student', '#$d123SA@dA2', 'sdf1123')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('port92', 'Pokolyuk W.K.', 3, 'student', 'dsfhsfdh4', 'asdwasd2000')");
        connector.executeStatement("INSERT INTO Users(login, name, group_id, user_type, password_salt, password_hash) VALUES('kolya073', 'Ivanko K.O.', 3, 'student', 'sdf34tgergf', 'kolya073123')");

        connector.executeStatement("INSERT INTO Users(login, name, user_type, password_salt, password_hash) VALUES('upa1221', 'Bandera S.A.','teacher', 'slava@asd', 'kwev3es')");
        connector.executeStatement("INSERT INTO Users(login, name, user_type,  password_salt, password_hash) VALUES('directoria1', 'Petliura S.V.', 'teacher', 'UNRsd8@asd', 'f;owejg89')");

        connector.executeStatement("INSERT INTO Users(login, name, user_type, password_salt, password_hash) VALUES('julius44', 'Julius Caesar', 'admin', 'rome3000', 'ADAsdas!3!@!#asd')");

        connector.executeStatement("INSERT INTO Lessons(id, date, time, homework, discipline, group_id, description, teacher_login) VALUES(1, date '2020-11-16', time '08:30-10:00', NULL, 'Mathematics', 1, NULL, 'upa1221')");
        connector.executeStatement("INSERT INTO Lessons(id, date, time, homework, discipline, group_id, description, teacher_login) VALUES(2, date '2020-11-16',  time '08:05-09:00', NULL, 'Databases', 1, NULL, 'upa1221')");
        connector.executeStatement("INSERT INTO Lessons(id, date, time, homework, discipline, group_id, description, teacher_login) VALUES(3, date '2020-11-16',  time '09:05-10:00', 'lab5', 'OOP', 2, NULL, 'directoria1')");
        connector.executeStatement("INSERT INTO Lessons(id, date, time, homework, discipline, group_id, description, teacher_login) VALUES(4, date '2020-11-16',  time '10:05-11:00', 'Test', 'English', 2, NULL, 'directoria1')");
        connector.executeStatement("INSERT INTO Lessons(id, date, time, homework, discipline, group_id, description, teacher_login) VALUES(5, date '2020-11-17',  time '11:05-12:00', NULL, 'National defence', 3, NULL, 'upa1221')");

        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(1, 'dominar3000')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(1, 'wqa092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(1, 'vasya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(1, 'eeesya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(2, 'dominar3000')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(2, 'wqa092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(2, 'vasya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(2, 'eeesya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(3, 'sssya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(3, 'dominat00')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(3, 'va9rkw2')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(3, 'wireas34')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(4, 'wireas34')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(4, 'dominat00')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(4, 'va9rkw2')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(4, 'sssya092')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(5, 'port92')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(5, 'kolya073')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(5, 'fsf1467')");
        connector.executeStatement("INSERT INTO Presents(lesson_id, login) VALUES(5, 'nsddw002')");

        connector.executeStatement("UPDATE Groups set grouphead_login = 'vasya092' WHERE id = 1");
        connector.executeStatement("UPDATE Groups set grouphead_login = 'sssya092' WHERE id = 2");
        connector.executeStatement("UPDATE Groups set grouphead_login = 'fsf1467' WHERE id = 3");

    }

    @Test
    public void findLessonsByGroupIdFromInterval() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate dateFrom = LocalDate.parse("2020-11-16", formatter);
        LocalDate dateTo= LocalDate.parse("2020-11-17", formatter);

        ArrayList<Lesson> lessons = (ArrayList<Lesson>) repository.findLessonsByGroupIdFromInterval("1", dateFrom, dateTo);

        Lesson lesson1 = lessons.get(0);

        assertEquals(Integer.valueOf(1), lesson1.getLessonId());
        assertEquals("2020-11-16T08:30", lesson1.getDateTime().toString());
        assertNull(lesson1.getHomework());
        assertEquals("Mathematics", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(1), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("upa1221", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("vasya092"));
        assertTrue(lesson1.getIsPresent().get("wqa092"));
        assertTrue(lesson1.getIsPresent().get("dominar3000"));
        assertTrue(lesson1.getIsPresent().get("eeesya092"));

        lesson1 = lessons.get(1);

        assertEquals(Integer.valueOf(2), lesson1.getLessonId());
        assertEquals("2020-11-16T08:05", lesson1.getDateTime().toString());
        assertNull(lesson1.getHomework());
        assertEquals("Databases", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(1), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("upa1221", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("vasya092"));
        assertTrue(lesson1.getIsPresent().get("wqa092"));
        assertTrue(lesson1.getIsPresent().get("dominar3000"));
        assertTrue(lesson1.getIsPresent().get("eeesya092"));
    }

    @Test
    public void findLessonsByTeacherIdFromInterval() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate dateFrom = LocalDate.parse("2020-11-16", formatter);
        LocalDate dateTo= LocalDate.parse("2020-11-17", formatter);

        ArrayList<Lesson> lessons = (ArrayList<Lesson>) repository.findLessonsByTeacherIdFromInterval("upa1221", dateFrom, dateTo);

        Lesson lesson1 = lessons.get(0);

        assertEquals(Integer.valueOf(1), lesson1.getLessonId());
        assertEquals("2020-11-16T08:30", lesson1.getDateTime().toString());
        assertNull(lesson1.getHomework());
        assertEquals("Mathematics", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(1), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("upa1221", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("vasya092"));
        assertTrue(lesson1.getIsPresent().get("wqa092"));
        assertTrue(lesson1.getIsPresent().get("dominar3000"));
        assertTrue(lesson1.getIsPresent().get("eeesya092"));

        lesson1 = lessons.get(1);

        assertEquals(Integer.valueOf(2), lesson1.getLessonId());
        assertEquals("2020-11-16T08:05", lesson1.getDateTime().toString());
        assertNull(lesson1.getHomework());
        assertEquals("Databases", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(1), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("upa1221", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("vasya092"));
        assertTrue(lesson1.getIsPresent().get("wqa092"));
        assertTrue(lesson1.getIsPresent().get("dominar3000"));
        assertTrue(lesson1.getIsPresent().get("eeesya092"));
    }


    @Test
    public void findAll() {
        ArrayList<Lesson> lessons = (ArrayList<Lesson>) repository.findAll();

        Lesson lesson1 = lessons.get(0);

        assertEquals(Integer.valueOf(1), lesson1.getLessonId());
        assertEquals("2020-11-16T08:30", lesson1.getDateTime().toString());
        assertNull(lesson1.getHomework());
        assertEquals("Mathematics", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(1), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("upa1221", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("vasya092"));
        assertTrue(lesson1.getIsPresent().get("wqa092"));
        assertTrue(lesson1.getIsPresent().get("dominar3000"));
        assertTrue(lesson1.getIsPresent().get("eeesya092"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate dateFrom = LocalDate.parse("2020-11-16", formatter);
        LocalDate dateTo= LocalDate.parse("2020-11-17", formatter);

        lesson1 = lessons.get(2);

        assertEquals(Integer.valueOf(3), lesson1.getLessonId());
        assertEquals("2020-11-16T09:05", lesson1.getDateTime().toString());
        assertEquals("lab5", lesson1.getHomework());
        assertEquals("OOP", lesson1.getDiscipline());
        assertEquals(Integer.valueOf(2), lesson1.getGroupId());
        assertNull(lesson1.getDescription());
        assertEquals("directoria1", lesson1.getTeacherLogin());

        assertTrue(lesson1.getIsPresent().get("sssya092"));
        assertTrue(lesson1.getIsPresent().get("dominat00"));
        assertTrue(lesson1.getIsPresent().get("va9rkw2"));
        assertTrue(lesson1.getIsPresent().get("wireas34"));

    }

    @Test
    public void deleteById() {
        try {
            Integer lessonId = 1;
            repository.deleteById(lessonId);
            ResultSet resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", lessonId));
            assertFalse(resultSet.next());

            lessonId = 2;
            repository.deleteById(lessonId);
            resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", lessonId));
            assertFalse(resultSet.next());

            lessonId = 3;
            repository.deleteById(lessonId);
            resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", lessonId));
            assertFalse(resultSet.next());

            lessonId = 20000;
            repository.deleteById(lessonId);
            resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", lessonId));
            assertFalse(resultSet.next());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void update() {
        String date = "2020-11-16";
        String time = "09:05:00";
        String datetime = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Integer lessonId = 1;
        LocalDateTime dateTime = LocalDateTime.parse(datetime, formatter);
        String description = "";
        String discipline = "Mathematics";
        String homework = "Do home work";
        Integer groupId = 1;
        String teacherLogin = "upa1221";
        HashMap<String, Boolean> isPresent = new HashMap<>();

        isPresent.put("dominar3000", true);
        isPresent.put("wqa092", false);
        isPresent.put("vasya092", false);
        isPresent.put("eeesya092", true);

        Lesson lesson = new Lesson(lessonId, dateTime, description, discipline, homework,
                groupId, teacherLogin, isPresent);

        repository.update(lesson);

        ResultSet resultSet = connector.executeStatement(String.format("SELECT * FROM Lessons WHERE id = %s", lessonId));

        try {
            while (resultSet.next()) {
                Integer idResult = resultSet.getInt(1);
                String dateResult = resultSet.getDate(2).toString();
                String timeResult = resultSet.getTime(3).toString();
                String descriptionResult = resultSet.getString(7);
                String disciplineResult = resultSet.getString(5);
                String homeworkResult = resultSet.getString(4);
                Integer groupIdResult = resultSet.getInt(6);
                String teacherLoginResult = resultSet.getString(8);

                assertEquals(idResult ,lessonId);
                assertEquals(dateResult ,date);
                assertEquals(timeResult ,time);
                assertEquals(descriptionResult ,description);
                assertEquals(disciplineResult ,discipline);
                assertEquals(homeworkResult ,homework);
                assertEquals(groupIdResult ,groupId);
                assertEquals(teacherLoginResult ,teacherLogin);

                ResultSet presentsResult1 = connector.executeStatement("SELECT * FROM Presents WHERE login = 'dominar3000' AND lesson_id = 1");
                assertTrue(presentsResult1.next());

                ResultSet presentsResult2 = connector.executeStatement(String.format("SELECT * FROM Presents WHERE login = 'wqa092' AND lesson_id = 1"));
                assertFalse(presentsResult2.next());

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void existsById() {
        boolean existsLesson1 = repository.existsById(2);
        boolean existsLesson2 = repository.existsById(3);
        boolean existsLesson3 = repository.existsById(5);

        assertTrue(existsLesson1);
        assertTrue(existsLesson2);
        assertTrue(existsLesson3);

        boolean existsLesson4 = repository.existsById(-5);
        boolean existsLesson5 = repository.existsById(50);
        boolean existsLesson6 = repository.existsById(20000);

        assertFalse(existsLesson4);
        assertFalse(existsLesson5);
        assertFalse(existsLesson6);
    }
    }