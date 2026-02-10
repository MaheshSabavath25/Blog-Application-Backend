package com.Blog_Application.BlogServices;

import java.util.List;
import com.Blog_Application.Payload.CommentDto;

public interface CommentService {

    // CREATE
    CommentDto addComment(Integer postId, String content, String email);

    // READ (owner check)
    List<CommentDto> getCommentsByPost(int postId, String email);

    // DELETE (owner only)
    void deleteComment(int commentId, String email);

	List<CommentDto> getCommentsByPost(int postId);

	void deleteComment(int commentId);

	List<CommentDto> getCommentsByPost(Integer postId);

	CommentDto createComment(CommentDto commentDto, int postId, String email);
	
	CommentDto updateComment(int commentId, String content, String email);
	// ✅ GET COMMENTS OF LOGGED-IN USER
	List<CommentDto> getCommentsByUser(String email);
	

	
	

	
	
	

	    void addComment(Integer postId, String content);

	    void replyToComment(Integer commentId, String content, String loggedInEmail);

	    void toggleCommentLike(Integer commentId, String loggedInEmail);

	    List<CommentDto> getCommentsByPost(Integer postId, String loggedInEmail);
	





}
