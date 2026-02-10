package com.Blog_Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Blog_Application.Entities.Comment;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

	List<Comment> findByPostIdAndParentIsNull(Integer postId);


    // ✅ ADD THIS
    List<Comment> findByUser(User user);
    List<Comment> findByParentId(Integer parentId);

	
}
