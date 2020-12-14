package exceptions;

import domain.User;

public class PermissionException extends IllegalAccessException {

    public PermissionException(String msg) {
        super(msg);
    }

    public static PermissionException notEnoughPermission(String user) {
        return new PermissionException("Not enough permission for user: " + user);
    }

    public static PermissionException notEnoughPermission(User user) {
        return new PermissionException("Not enough permission for user: " + user.getLogin());
    }
}
