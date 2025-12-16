package com.example.genesis.controller;

import com.example.genesis.dto.UserBasicDto;
import com.example.genesis.dto.UserResponseDto;
import com.example.genesis.model.User;
import com.example.genesis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody User userRequest) {
        User createdUser = userService.createUser(new User(
                userRequest.getName(),
                userRequest.getSurname(),
                userRequest.getPersonId()
        ));
        
        return new ResponseEntity<>(toDetailDto(createdUser), HttpStatus.CREATED);
    }
    @GetMapping
    public List<?> getAllUsers(@RequestParam(required = false, defaultValue = "false") boolean detail) {
        List<User> users = userService.getAllUsers();
        
        if (detail) {
            return users.stream().map(this::toDetailDto).collect(Collectors.toList());
        } else {
            return users.stream().map(this::toBasicDto).collect(Collectors.toList());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean detail) {
        
        User user = userService.getUserById(id);
        
        if (detail) {
            return ResponseEntity.ok(toDetailDto(user));
        } else {
            return ResponseEntity.ok(toBasicDto(user));
        }
    }
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody User userRequest) {
        User updatedUser = userService.updateUser(userRequest.getId(), userRequest);
        return ResponseEntity.ok(toDetailDto(updatedUser));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    private UserResponseDto toDetailDto(User user) {
        return new UserResponseDto(
                user.getId(), user.getName(), user.getSurname(), user.getPersonId(), user.getUuid());
    }
    private UserBasicDto toBasicDto(User user) {
        return new UserBasicDto(
                user.getId(), user.getName(), user.getSurname());
    }
}