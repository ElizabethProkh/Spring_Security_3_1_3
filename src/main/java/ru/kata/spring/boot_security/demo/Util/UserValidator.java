package ru.kata.spring.boot_security.demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Services.userDetailServiceImpl;


@Component
public class UserValidator implements Validator {

    private final userDetailServiceImpl userDetailService;

    @Autowired
    public UserValidator(userDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            userDetailService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored){
            return; //здесь все ок, пользователь не найден
        }

        errors.rejectValue("username","", "Человек с таким именем пользователя уже сущестувет");

    }
}
