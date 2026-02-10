package com.Blog_Application.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;
import com.Blog_Application.Entities.Category;

public interface PostRepo extends JpaRepository<Post, Integer> {

    // 🔹 Get posts by user
    List<Post> findByUser(User user);

    // 🔹 Get posts by category
    List<Post> findByCategory(Category category);
    
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String title, String content
    );
    
    List<Post> findByHashtags_Name(String name);

    
    
}
