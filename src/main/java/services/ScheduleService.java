package services;

import domain.Lesson;
import storage.LessonRepository;

import java.time.LocalDate;
import java.util.List;

public class ScheduleService {

    private final LessonRepository lessonRepository;

    public ScheduleService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    List<Lesson> getTeacherSchedule(String teacherID) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusWeeks(2);
        return lessonRepository.findLessonsByTeacherIdFromInterval(teacherID, from, to);
    }

    List<Lesson> getGroupSchedule(String groupId) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusWeeks(2);
        return lessonRepository.findLessonsByGroupIdFromInterval(groupId, from, to);
    }
}
