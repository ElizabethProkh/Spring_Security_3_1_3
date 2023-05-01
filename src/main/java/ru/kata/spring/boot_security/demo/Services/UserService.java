package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.Models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    void addUser (User user);

    Optional<User> getUserById(Long id);

    void updateUser (User updatedUser, Long id);

    void deleteUser(Long id);

    User findUserByName(String name);
}
