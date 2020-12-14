package services;

import domain.User;
import storage.UserRepository;

import java.util.Optional;

import static exceptions.EntityNotExistException.*;

public class AuthenticationService {

    UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    boolean verifyUser(User user) {
        Optional<User> validUser = userRepository.findById(user.getLogin());
        return validUser.isPresent() && validUser.get().getPassword().equals(user.getPassword());
    }
}
