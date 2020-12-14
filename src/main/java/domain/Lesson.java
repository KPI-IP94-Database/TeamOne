package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Lesson {

    private Integer lessonId;
    private LocalDateTime dateTime;
    private String description;
    private String discipline;
    private String homework;
    private Integer groupId;
    private String teacherLogin;
    private final Map<String, Boolean> isPresent;

    public boolean isLessonTeacher(User user) {
        return user.isTeacher() && teacherLogin.equals(user.getLogin());
    }

    public boolean removePresent(String studentLogin) {
        return isPresent.replace(studentLogin, isPresent(studentLogin), false);
    }

    public void addPresent(String studentLogin) {
        isPresent.put(studentLogin, true);
    }

    public boolean isPresent(String studentLogin) {
        return isPresent.getOrDefault(studentLogin, false);
    }

    public Set<String> getPresentStudentLogins() {
        return isPresent.keySet().stream().filter(this::isPresent).collect(Collectors.toSet());
    }
}
