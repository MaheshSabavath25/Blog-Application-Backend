package com.Blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.AuthToken;

public interface AuthTokenRepo extends JpaRepository<AuthToken, Integer> {

    Optional<AuthToken> findByToken(String token);
}
