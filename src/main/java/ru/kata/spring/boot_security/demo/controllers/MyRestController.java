package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exeptions.NoSuchUserException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MyRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/userInfo")
    public User getUserInfo(Principal principal) {
        User user = userService.findUserByName(principal.getName());
        return user;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return allUsers;
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NoSuchUserException("There is no user with id: " + id + " in database");
        }
        return user;
    }

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        Role role = roleService.findByName(user.getNewRole());
        user.setRoles(Collections.singleton(role));
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (user.getNewRole() == null) {
            user.setRoles(userService.getUserById(user.getId()).get().getRoles());
            return userService.updateUser(user, user.getId());
        } else {
            Role role = roleService.findByName(user.getNewRole());
            user.setRoles(Collections.singleton(role));
            return userService.updateUser(user, user.getId());
        }
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NoSuchUserException("There is no user with id: " + id + " in database");
        }
        userService.deleteUser(id);
    }
}
