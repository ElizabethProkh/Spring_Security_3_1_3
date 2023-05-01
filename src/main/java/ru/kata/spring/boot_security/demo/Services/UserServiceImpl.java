package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);

    }

    @Override
    @Transactional
    public void updateUser(User updatedUser, Long id) {
        // Получаем существующего пользователя по id
        User existingUser = userRepository.findById(updatedUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Обновляем поля пользователя
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setDepartment(updatedUser.getDepartment());
        existingUser.setPassword(updatedUser.getPassword());

        // Заменяем роли пользователя на новые роли
        List<Role> newRoles = new ArrayList<>();
        for (Role role : updatedUser.getRoles()) {
            Role newRole = roleRepository.findByName(role.getName());
            newRoles.add(newRole);
        }
        existingUser.setRoles(newRoles);

        // Сохраняем обновленного пользователя в базе данных
        userRepository.saveAndFlush(existingUser);
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
