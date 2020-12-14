package exceptions;

import domain.Group;
import domain.Lesson;
import domain.User;

public class EntityExistException extends IllegalArgumentException {

    public final String entityType;

    EntityExistException(String entityType, String entityId) {
        super(entityType + " already exists: " + entityId);
        this.entityType = entityType;
    }

    public static EntityExistException groupAlreadyExists(Integer entityId) {
        return new EntityExistException("Group", entityId.toString());
    }

    public static EntityExistException lessonAlreadyExists(Integer entityId) {
        return new EntityExistException("Lesson", entityId.toString());
    }

    public static EntityExistException userAlreadyExists(String login) {
        return new EntityExistException("User", login);
    }

    public static EntityExistException groupAlreadyExists(Group group) {
        return groupAlreadyExists(group.getId());
    }

    public static EntityExistException lessonAlreadyExists(Lesson lesson) {
        return lessonAlreadyExists(lesson.getLessonId());
    }

    public static EntityExistException userAlreadyExists(User user) {
        return userAlreadyExists(user.getLogin());
    }
}
