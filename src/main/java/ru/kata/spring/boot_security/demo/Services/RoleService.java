package ru.kata.spring.boot_security.demo.Services;

import ru.kata.spring.boot_security.demo.Models.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();
    Role findByName(String name);

    Role findRoleById(Long id);

}
