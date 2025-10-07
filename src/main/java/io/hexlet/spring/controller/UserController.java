package io.hexlet.spring.controller;

import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/users")
public class UserController {

//    private final UserRepository userRepository;
//
//    public UserController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Autowired
    private UserRepository userRepository;

//    // GET /api/users - получить всех пользователей
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userRepository.findAll(); // 200 OK
//    }
    @GetMapping("")
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(this::toDTO)
                .toList();
        return result;
    }

    // POST /api/users - создать пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user); // 201 Created
    }

//    // GET /api/users/{id} - получить пользователя по ID
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUser(@PathVariable Long id) {
//        var user = userRepository.findById(id);
//
//        return user.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build()); // 200 OK или 404 Not Found
//    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    // Пользователь автоматически преобразуется в JSON
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        return dto;
    }

    // PUT /api/users/{id} - обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userData) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFirstName(userData.getFirstName());
            user.setLastName(userData.getLastName());
            user.setEmail(userData.getEmail());
            userRepository.save(user);
            return ResponseEntity.ok(user); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // DELETE /api/users/{id} - удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }


    // Чтобы сделать работу удобнее
    // И избежать дублирования
    private UserDTO toDTO(User user) {
        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
