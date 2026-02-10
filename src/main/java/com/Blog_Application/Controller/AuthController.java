package com.Blog_Application.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.Blog_Application.BlogServices.UserServices;
import com.Blog_Application.Payload.JwtAuthRequest;
import com.Blog_Application.Payload.JwtAuthResponse;
import com.Blog_Application.Payload.UserDto;
import com.Blog_Application.Security.CustomUserDetailsService;
import com.Blog_Application.Security.JwtTokenHelper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserServices userService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @RequestBody UserDto userDto) {

        UserDto registeredUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(
            @RequestBody JwtAuthRequest request) {

        // 1️⃣ Authenticate user
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );

        authenticationManager.authenticate(authenticationToken);

        // 2️⃣ Load user details
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());

        // 3️⃣ Generate JWT token
        String token = jwtTokenHelper.generateToken(userDetails);

        // 4️⃣ Send response
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
