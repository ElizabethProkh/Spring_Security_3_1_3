package ru.kata.spring.boot_security.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Models.Role;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Services.RoleService;
import ru.kata.spring.boot_security.demo.Services.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

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
        model.addAttribute("roles",roleService.getAllRoles());
        String name = authentication.getName();
        User user = userService.findUserByName(name);
        model.addAttribute("user", user);
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "allUsers";
    }


    @GetMapping("/new")
    public String addNewUser(Model model, Authentication authentication) {
        User newUser = new User();
        newUser.setRoles(new ArrayList<Role>());
        model.addAttribute("newUser", newUser);
        model.addAttribute("roles", roleService.getAllRoles());

        String name = authentication.getName();
        User user = userService.findUserByName(name);
        model.addAttribute("user", user);
        return "addNewUser";
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("newUser") User user, @RequestParam("role") String roleName) {
        Role role = roleService.findByName(roleName);
        user.setRoles(Collections.singletonList(role));
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("User", userService.getUserById(id).orElse(new User()));
        model.addAttribute("roles",roleService.getAllRoles());
        return "userEdit";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("User") User user, @PathVariable("id") Long id, @RequestParam("roles") Long roleId) {
        Role role = roleService.findRoleById(roleId);
        user.setRoles(Collections.singletonList(role));
        userService.updateUser(user, id);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    private void getUserRoles(User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.findByName(role.getName()))
                .collect(Collectors.toList()));
    }
}
