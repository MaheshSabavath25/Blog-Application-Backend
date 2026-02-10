package com.Blog_Application.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Blog_Application.BlogServices.UserServices;
import com.Blog_Application.Payload.ApiResponse;
import com.Blog_Application.Payload.UserDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    // ✅ CREATE USER
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto) {

        UserDto createdUser = userServices.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // ✅ UPDATE USER
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid @RequestBody UserDto userDto,
            @PathVariable int userId) {

        UserDto updatedUser =
                userServices.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    // ✅ GET USER BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(
            @PathVariable int userId) {

        return ResponseEntity.ok(
                userServices.getUserById(userId)
        );
    }

    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return ResponseEntity.ok(
                userServices.getAllUsers()
        );
    }

    // ✅ DELETE USER
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable int userId) {

        userServices.deleteUser(userId);

        return ResponseEntity.ok(
                new ApiResponse("User deleted successfully", true)
        );
    }
}
