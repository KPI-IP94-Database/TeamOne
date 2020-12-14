package services;


import domain.User;
import exceptions.PermissionException;
import storage.UserRepository;

import java.util.Optional;

import static exceptions.EntityExistException.userAlreadyExists;
import static exceptions.EntityNotExistException.userIsNotExist;
import static exceptions.PermissionException.notEnoughPermission;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void saveNewUser(User caller, User newUser) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller);
        }
        if (userRepository.existsById(newUser.getLogin())) {
            throw userAlreadyExists(newUser);
        }
        userRepository.saveNewEntity(newUser);
    }

    void deleteUser(User caller, String login) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller);
        }
        if (!userRepository.existsById(login)) {
            throw userIsNotExist(login);
        }
        userRepository.deleteById(login);
    }

    void editUser(User caller, User editedUser) throws PermissionException {
        boolean callerIsUser = caller.getLogin().equals(editedUser.getLogin());
        boolean callerIsAdmin = caller.isAdmin();

        if (!(callerIsAdmin || callerIsUser)) {
            throw notEnoughPermission(caller);
        }

        User user = userRepository.findById(editedUser.getLogin())
                .orElseThrow(() -> userIsNotExist(editedUser));

        boolean typeIsChanged = !user.getUserType().equals(editedUser.getUserType());
        boolean loginIsChanged = !user.getLogin().equals(editedUser.getLogin());
        boolean groupIdIsChanged = !user.getGroupId().equals(editedUser.getGroupId());

        if ((typeIsChanged || loginIsChanged || groupIdIsChanged) && !callerIsAdmin) {
            throw notEnoughPermission(caller);
        }

        userRepository.update(editedUser);
    }

    Optional<User> getUserByLogin(String login) {
        return userRepository.findById(login);
    }
}
