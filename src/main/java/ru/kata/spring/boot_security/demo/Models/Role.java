package ru.kata.spring.boot_security.demo.Models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.print.DocFlavor;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        String role = getName();
        if(role.equals("ROLE_ADMIN")){
            return "ADMIN";
        }
        if (role.equals("ROLE_USER")){
            return "USER";
        }
        return role;
    }
}
