package com.Blog_Application.Repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // ✅ correct – NO @Query needed
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);
}
