package io.hexlet.spring.controler;

import io.hexlet.spring.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // GET /api/users - получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return users; // 200 OK
    }

    // POST /api/users - создать пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setId(idCounter.getAndIncrement());
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201 Created
    }

    // GET /api/users/{id} - получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 200 OK или 404 Not Found
    }

    // PUT /api/users/{id} - обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userData) {
        Optional<User> existingUser = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userData.getName());
            user.setEmail(userData.getEmail());
            return ResponseEntity.ok(user); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // DELETE /api/users/{id} - удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
