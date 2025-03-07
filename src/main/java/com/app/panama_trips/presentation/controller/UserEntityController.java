package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.service.implementation.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping
    public ResponseEntity<Page<UserEntity>> findAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination) {

        return ResponseEntity.ok(userEntityService.getAllUser(page, size, enabledPagination));
    }

    @PostMapping
    public ResponseEntity<UserEntity> saveUser(@RequestBody AuthCreateUserRequest authCreateUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.saveUser(authCreateUserRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findUSerById(@PathVariable Long id) {
        return ResponseEntity.ok(userEntityService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updatedUser(@PathVariable Long id, @RequestBody AuthCreateUserRequest authCreateUserRequest) {
        return ResponseEntity.ok(userEntityService.updateUser(id, authCreateUserRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        userEntityService.deleteUser(id);
        return ResponseEntity.ok(Map.of("deleted", Boolean.TRUE));
    }
}
