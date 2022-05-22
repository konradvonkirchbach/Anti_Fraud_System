package antifraud.service;

import antifraud.model.user.LockEnum;
import antifraud.model.user.RoleEnum;
import antifraud.model.user.User;
import antifraud.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean userExists(User user) {
        User other = userRepository.findByUsername(user.getUsername());
        if (other == null) {
            return false;
        }
        return user.equals(other);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(setRolesAndLock(user));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    private User setRolesAndLock(User user) {
        if (userRepository.findAll().isEmpty()) {
            user.setRole(RoleEnum.ADMINISTRATOR);
            user.setLockEnum(LockEnum.UNLOCK);
        } else {
            user.setRole(RoleEnum.MERCHANT);
            user.setLockEnum(LockEnum.LOCK);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean userExists(String username) {
        User user = findByUsername(username);
        return user != null;
    }

    public User deleteUser(String username) {
        User user = findByUsername(username);
        userRepository.delete(user);
        return user;
    }
}
