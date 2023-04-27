package ru.kata.spring.boot_security.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Models.User;
import ru.kata.spring.boot_security.demo.Services.RoleService;
import ru.kata.spring.boot_security.demo.Services.UserService;

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
    public String showUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "allUsers";
    }


    @GetMapping("/admin/add")
    public String addNewUser(Model model) {
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        return "addNewUser";
    }

    @PostMapping("/admin/add")
    public String saveUser(@ModelAttribute("newUser") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id).orElse(new User()));
        model.addAttribute("roles",roleService.getAllRoles());
        return "userEdit";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
