package storage;

import domain.Lesson;
import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson, Integer> {

    List<Lesson> findLessonsByGroupIdFromInterval(String groupId, LocalDate from, LocalDate to);

    List<Lesson> findLessonsByTeacherIdFromInterval(String teacherLogin, LocalDate from, LocalDate to);
}
