package services;

import domain.Group;
import domain.Lesson;
import domain.User;
import exceptions.PermissionException;
import storage.GroupRepository;
import storage.LessonRepository;

import java.util.Set;

import static exceptions.EntityNotExistException.*;
import static exceptions.EntityNotExistException.lessonIsNotExist;
import static exceptions.PermissionException.*;

public class LessonService {

    private final LessonRepository lessonRepository;
    private final GroupRepository groupRepository;

    LessonService(LessonRepository lessonRepository, GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
    }

    void setLessonHomework(User caller, Integer lessonId, String homework) throws PermissionException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> lessonIsNotExist(lessonId));
        Group group = groupRepository.findById(lesson.getGroupId())
                .orElseThrow(() -> groupIsNotExist(lesson.getGroupId()));

        if (!isTeacherOrAdminOrGrouphead(caller, group, lesson)) {
            throw notEnoughPermission(caller.getLogin());
        }

        lesson.setHomework(homework);
        lessonRepository.update(lesson);
    }

    Lesson getLessonToEditPresence(Integer lessonId) throws IllegalArgumentException {
        return lessonRepository
                .findById(lessonId)
                .orElseThrow(() -> lessonIsNotExist(lessonId));
    }

    void saveLesson(User caller, Lesson lesson) throws PermissionException {
        Group group = groupRepository.findById(lesson.getGroupId())
                .orElseThrow(() -> groupIsNotExist(lesson.getGroupId()));

        if (!(isTeacherOrAdminOrGrouphead(caller, group, lesson))) {
            throw notEnoughPermission(caller.getLogin());
        }

        lesson.setLessonId(null);
        lessonRepository.saveNewEntity(lesson);
    }

    void deleteLesson(User caller, Integer lessonId) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller.getLogin());
        }
        if (!lessonRepository.existsById(lessonId)) {
            throw lessonIsNotExist(lessonId);
        }
        lessonRepository.deleteById(lessonId);
    }

    void editLesson(User caller, Lesson lesson) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller.getLogin());
        }
        if (!lessonRepository.existsById(lesson.getLessonId())) {
            throw lessonIsNotExist(lesson);
        }
        lessonRepository.update(lesson);
    }

    void removeStudentPresence(User caller, Integer lessonId, String studentLogin) throws PermissionException {
        Lesson lesson = getLessonToEditPresence(caller, lessonId);
        lesson.removePresent(studentLogin);
        lessonRepository.update(lesson);
    }

    void addStudentPresence(User caller, Integer lessonId, String studentLogin) throws PermissionException {
        Lesson lesson = getLessonToEditPresence(caller, lessonId);
        lesson.addPresent(studentLogin);
        lessonRepository.update(lesson);
    }

    private Lesson getLessonToEditPresence(User caller, Integer lessonId) throws PermissionException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> lessonIsNotExist(lessonId));
        Group group = groupRepository.findById(lesson.getGroupId())
                .orElseThrow(() -> groupIsNotExist(lesson.getGroupId()));
        if (!isTeacherOrAdminOrGrouphead(caller, group, lesson)) {
            throw notEnoughPermission(caller.getLogin());
        }
        return lesson;
    }

    Set<String> getPresence(User caller, Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> lessonIsNotExist(lessonId));
        return lesson.getPresentStudentLogins();
    }

    private boolean isTeacherOrAdminOrGrouphead(User user, Group group, Lesson lesson) {
        return lesson.isLessonTeacher(user) || user.isAdmin() || group.isGrouphead(user);
    }
}
