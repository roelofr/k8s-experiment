package dev.roelofr.k8s.web.controller;

import dev.roelofr.k8s.domain.User;
import dev.roelofr.k8s.dto.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User[]> getAll() {
        return ResponseEntity.ok(
            userRepository.findAll().toArray(new User[0])
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable int id) {
        var result = userRepository.findById(id);

        if (result.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userRepository.save(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        userRepository.delete(id);

        return ResponseEntity.status(HttpStatus.RESET_CONTENT).build();
    }
}
