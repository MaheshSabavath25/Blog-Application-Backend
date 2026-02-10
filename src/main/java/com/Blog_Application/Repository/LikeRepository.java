package com.Blog_Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.Like;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    long countByPost(Post post);

	boolean existsByPostAndUser(Post post, User user);
}
