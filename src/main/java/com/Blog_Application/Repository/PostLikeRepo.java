package com.Blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.PostLike;
import com.Blog_Application.Entities.User;

import jakarta.transaction.Transactional;

@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Integer> {

    Optional<PostLike> findByPostAndUser(Post post, User user);

    long countByPost(Post post);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostLike pl WHERE pl.post = :post AND pl.user = :user")
    void deleteByPostAndUser(
            @Param("post") Post post,
            @Param("user") User user
    );
}


