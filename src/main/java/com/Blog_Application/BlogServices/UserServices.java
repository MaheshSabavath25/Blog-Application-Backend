package com.Blog_Application.BlogServices;

import java.util.List;

import com.Blog_Application.Payload.UserDto;

import jakarta.validation.Valid;

public interface UserServices {

    UserDto registerNewUser(UserDto userDto);

    List<UserDto> getAllUsers();   // 👈 THIS ONE

	UserDto createUser(@Valid UserDto userDto);

	UserDto updateUser(@Valid UserDto userDto, int userId);

	Object getUserById(int userId);

	void deleteUser(int userId);
}


