package exceptions;

import domain.Group;
import domain.Lesson;
import domain.User;

public class EntityNotExistException extends IllegalArgumentException {

    public final String entityType;

    EntityNotExistException(String entityType, String entityId) {
        super(entityType + " doesn't exist: " + entityId);
        this.entityType = entityType;
    }

    public static EntityNotExistException groupIsNotExist(Integer entityId) {
        return new EntityNotExistException("Group", entityId.toString());
    }

    public static EntityNotExistException lessonIsNotExist(Integer entityId) {
        return new EntityNotExistException("Lesson", entityId.toString());
    }

    public static EntityNotExistException userIsNotExist(String login) {
        return new EntityNotExistException("User", login);
    }

    public static EntityNotExistException groupIsNotExist(Group group) {
        return groupIsNotExist(group.getId());
    }

    public static EntityNotExistException lessonIsNotExist(Lesson lesson) {
        return lessonIsNotExist(lesson.getLessonId());
    }

    public static EntityNotExistException userIsNotExist(User user) {
        return userIsNotExist(user.getLogin());
    }
}
