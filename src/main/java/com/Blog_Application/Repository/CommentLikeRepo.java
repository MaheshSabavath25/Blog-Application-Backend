package com.Blog_Application.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Blog_Application.Entities.CommentLike;

import jakarta.transaction.Transactional;

@Repository
public interface CommentLikeRepo extends JpaRepository<CommentLike, Integer> {

    // 🔢 like count
    long countByCommentId(int commentId);

    // ❤️ find like (USED FOR TOGGLE)
    Optional<CommentLike> findByCommentIdAndUserId(int commentId, int userId);

    // ❓ check liked
    boolean existsByCommentIdAndUserId(int commentId, int userId);

    // 🗑 delete one user’s like (unlike)
    void deleteByCommentIdAndUserId(int commentId, int userId);

    // 🧹 delete all likes of a comment (used before deleting comment)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM comment_like WHERE comment_id = :commentId", nativeQuery = true)
    void deleteAllByCommentId(@Param("commentId") Integer commentId);
   

       

        // delete likes of a comment
        void deleteByComment_Id(int commentId);
        
        long countByComment_Id(int commentId);

        boolean existsByComment_IdAndUser_Id(int commentId, int userId);

        void deleteByComment_IdAndUser_Id(int commentId, int userId);
    }


