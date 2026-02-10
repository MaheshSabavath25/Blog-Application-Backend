package com.Blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.Hashtag;

public interface HashtagRepo extends JpaRepository<Hashtag, Integer> {

    Optional<Hashtag> findByName(String name);
}
