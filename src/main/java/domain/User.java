package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private String login;
    private String password;
    private String name;
    private UserType userType;
    private Integer groupId;

    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }

    public boolean isTeacher() {
        return userType == UserType.TEACHER;
    }

    public boolean isGrouphead() {
        return userType == UserType.GROUP_HEAD;
    }

    public boolean isStudent() {
        return userType == UserType.STUDENT;
    }

    public boolean isUnknown() {
        return userType == UserType.Unknown;
    }
}
