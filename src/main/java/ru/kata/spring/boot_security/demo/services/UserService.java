package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User addUser(User user);

    Optional<User> getUserById(Long id);

    User updateUser(User updatedUser, Long id);

    void deleteUser(Long id);

    User findUserByName(String name);
}
