package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Repositories.UserRepository;
import ru.kata.spring.boot_security.demo.Util.UserValidator;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    @Transactional
    public void register(User user){
        Errors error = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user,error);
        if(error.hasErrors()){
            try {
                throw new ValidationException(error.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")));
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword()); //метод encode зашифрует пароль
        user.setPassword(encodedPassword);

        List<Role> userRoles = new ArrayList<>();
        Role role = new Role();
        role.setName("ROLE_USER");
        userRoles.add(role);
        user.setRoles(userRoles); //выдаем роль user при регистрации новому пользователю

        userRepository.save(user);
    }
}
