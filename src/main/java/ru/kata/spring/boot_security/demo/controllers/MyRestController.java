package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.exeptions.NoSuchUserException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@Secured("ROLE_ADMIN")
@RequestMapping("/api/admin")
public class MyRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MyRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        Optional<User> oldUser = userService.getUserById(user.getId());
        if (oldUser.isEmpty()) {
            Role role = roleService.findByName(user.getNewRole());
            user.setRoles(Collections.singleton(role));
            return new ResponseEntity<>(userService.addUser(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user, user.getId());
        return updatedUser;
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NoSuchUserException("There is no user with id: " + id + " in database");
        }
        userService.deleteUser(id);
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        return roles;
    }
}
