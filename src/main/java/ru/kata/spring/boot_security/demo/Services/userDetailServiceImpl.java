package ru.kata.spring.boot_security.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Repositories.UserRepository;

@Service
public class userDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public userDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //метод возвращает по имени пользователя самого юзера из базы данных
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", name));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRoles());
    }

}
