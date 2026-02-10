package com.Blog_Application.BlogServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Blog_Application.Entities.User;
import com.Blog_Application.Payload.UserDto;
import com.Blog_Application.Repositorys.UserRepository;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserDto userDto) {
        logger.info("Received registration request: {}", userDto);

        if(userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            logger.error("Password is null or empty for user: {}", userDto.getEmail());
            throw new RuntimeException("Password cannot be null or empty");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        logger.info("Saving user: {} with encoded password: {}", user.getEmail(), user.getPassword());
        User savedUser = userRepository.save(user);
        logger.info("User saved with ID: {}", savedUser.getId());

        return savedUser;
    }
}

