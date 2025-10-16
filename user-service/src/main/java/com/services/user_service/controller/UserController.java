package com.services.user_service.controller;


import com.services.user_service.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private Map<Long, User> users = new HashMap<>();

    public UserController() {
        users.put(1L, new User(1L, "Juan Pérez", "juan@email.com"));
        users.put(2L, new User(2L, "María García", "maria@email.com"));
        users.put(3L, new User(3L, "Carlos López", "carlos@email.com"));
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        System.out.println("Recibiendo solicitud para usuario: " + id);
        User user = users.get(id);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
