package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exeptions.NoSuchUserException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);

    }

    @Override
    @Transactional
    public User updateUser(User updatedUser, Long id) {
        if (updatedUser.getNewRole() == null) {
            User existingUser = getUserById(updatedUser.getId()).orElseThrow(() -> new NoSuchUserException("User not found"));
            updatedUser.setRoles(existingUser.getRoles());
        } else {
            Role role = roleRepository.findByName(updatedUser.getNewRole());
            updatedUser.setRoles(Collections.singleton(role));
        }
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        return userRepository.saveAndFlush(updatedUser);
    }


    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }
}
