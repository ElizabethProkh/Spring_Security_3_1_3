package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Collections;
import java.util.HashSet;

@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin")
    public String showUsers(Model model, Authentication authentication) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.findUserByName(authentication.getName()));
        model.addAttribute("newUser", new User());
        return "allUsers";
    }


    @GetMapping("/new")
    public String addNewUser(Model model, Authentication authentication) {
        User newUser = new User(new HashSet<>());
        model.addAttribute("newUser", newUser);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.findUserByName(authentication.getName()));
        return "addNewUser";
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("newUser") User user, @RequestParam("role") String roleName) {
        user.setRoles(Collections.singleton(roleService.findByName(roleName)));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("User", userService.getUserById(id).orElse(new User()));
        model.addAttribute("roles", roleService.getAllRoles());
        return "userEdit";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("User") User user, @PathVariable("id") Long id, @RequestParam("roles") Long roleId) {
        user.setRoles(Collections.singleton(roleService.findRoleById(roleId)));
        userService.updateUser(user, id);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
