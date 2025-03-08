package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.service.implementation.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for users")
public class UserEntityController {

    private final UserEntityService userEntityService;

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Get all users in the system",
            tags = {"User"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserEntity.class)
                    )
            )
    )
    public ResponseEntity<Page<UserEntity>> findAllUsers(
            @Parameter(description = "Page number (default 0)")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Number of users per page (default: 10)", example = "10")
            @RequestParam(defaultValue = "10") Integer size,

            @Parameter(description = "Enable pagination (default: false)", example = "false")
            @RequestParam(defaultValue = "false") Boolean enabledPagination) {

        return ResponseEntity.ok(userEntityService.getAllUser(page, size, enabledPagination));
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Create a new user in the system",
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthCreateUserRequest.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserEntity.class)
                    )
            )
    )
    public ResponseEntity<UserEntity> saveUser(@Valid @RequestBody AuthCreateUserRequest authCreateUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.saveUser(authCreateUserRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a user by id",
            description = "Get a user in the system by its id",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User found",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    public ResponseEntity<UserEntity> findUSerById(@PathVariable Long id) {
        return ResponseEntity.ok(userEntityService.getUserById(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a user by id",
            description = "Update a user in the system by its id",
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User data to update",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthCreateUserRequest.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserEntity.class)
                    )
            )
    )
    public ResponseEntity<UserEntity> updatedUser(@PathVariable Long id, @RequestBody AuthCreateUserRequest authCreateUserRequest) {
        return ResponseEntity.ok(userEntityService.updateUser(id, authCreateUserRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user by id",
            description = "Delete a user in the system by its id",
            tags = {"User"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User deleted",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Map.class)
                    )
            )
    )
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
        userEntityService.deleteUser(id);
        return ResponseEntity.ok(Map.of("deleted", Boolean.TRUE));
    }
}
