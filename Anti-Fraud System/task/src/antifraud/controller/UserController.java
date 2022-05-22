package antifraud.controller;

import antifraud.model.user.*;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private UserService userService;

    private PasswordEncoder encoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        if (userService.userExists(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserResponse userResponse = new UserResponse(userService.saveUser(user));

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<UserResponse> grantRole(@RequestBody UserGrantRole userGrantRole) {
        if (!userService.userExists(userGrantRole.getUsername())) {
            return ResponseEntity.notFound().build();
        }

        if (userGrantRole.getRole() == RoleEnum.ADMINISTRATOR) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.findByUsername(userGrantRole.getUsername());
        if (userGrantRole.getRole() == user.getRole()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        user.setRole(userGrantRole.getRole());
        UserResponse userResponse = new UserResponse(userService.updateUser(user));
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/api/auth/access")
    public ResponseEntity<UserSetLockResponse> setAccess(@RequestBody UserSetLockRequest request) {
        if (!userService.userExists(request.getUsername())) {
            return ResponseEntity.notFound().build();
        }
        User user = userService.findByUsername(request.getUsername());
        if (user.getRole() == RoleEnum.ADMINISTRATOR) {
            return ResponseEntity.badRequest().build();
        }
        user.setLockEnum(request.getOperation());
        UserSetLockResponse response = new UserSetLockResponse(userService.updateUser(user));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var userResponses = userService.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<DeleteUserResponse> deleteUser(@PathVariable String username) {
        if (!userService.userExists(username)) {
            return ResponseEntity.notFound().build();
        }
        User user = userService.deleteUser(username);
        return ResponseEntity.ok(new DeleteUserResponse(user));
    }

}
