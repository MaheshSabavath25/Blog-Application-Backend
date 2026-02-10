package com.Blog_Application.Services.Imple;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Blog_Application.BlogServices.CommentService;
import com.Blog_Application.Entities.Comment;
import com.Blog_Application.Entities.CommentLike;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;
import com.Blog_Application.Exception.ResourceNotFoundException;
import com.Blog_Application.Payload.CommentDto;
import com.Blog_Application.Repository.CommentLikeRepo;
import com.Blog_Application.Repository.CommentRepo;
import com.Blog_Application.Repository.PostRepo;
import com.Blog_Application.Repositorys.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentLikeRepo commentLikeRepo;

    @Autowired
    private UserRepository userRepo;

    /* ================= HELPER ================= */

    private String getLoggedInEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private User getLoggedInUser() {
        return userRepo.findByEmail(getLoggedInEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /* ================= ADD COMMENT ================= */

    @Override
    public CommentDto addComment(Integer postId, String content, String email) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "id", postId));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "email", email));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);

        return mapToDto(commentRepo.save(comment), user);
    }

    /* ================= GET COMMENTS BY POST ================= */

    @Override
    public List<CommentDto> getCommentsByPost(Integer postId, String loggedInEmail) {

        User currentUser = userRepo.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return commentRepo.findByPostIdAndParentIsNull(postId)
                .stream()
                .map(comment -> mapToDto(comment, currentUser))
                .toList();
    }

    /* ================= UPDATE COMMENT ================= */

    @Override
    public CommentDto updateComment(int commentId, String content, String email) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to edit this comment");
        }

        comment.setContent(content);
        return mapToDto(commentRepo.save(comment), comment.getUser());
    }

    /* ================= DELETE COMMENT ================= */

    @Override
    @Transactional
    public void deleteComment(int commentId, String email) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "id", commentId));

        if (!comment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }

        // 1️⃣ delete likes of replies
        List<Comment> replies = commentRepo.findByParentId(commentId);
        for (Comment reply : replies) {
            commentLikeRepo.deleteByComment_Id(reply.getId());
        }

        // 2️⃣ delete replies
        commentRepo.deleteAll(replies);

        // 3️⃣ delete likes of main comment
        commentLikeRepo.deleteByComment_Id(commentId);

        // 4️⃣ delete main comment
        commentRepo.delete(comment);
    }




    @Override
    public void deleteComment(int commentId) {
        deleteComment(commentId, getLoggedInEmail());
    }

    /* ================= COMMENT LIKE ================= */

    @Override
    @Transactional
    public void toggleCommentLike(Integer commentId, String email) {

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyLiked =
                commentLikeRepo.existsByComment_IdAndUser_Id(
                        commentId,
                        user.getId()
                );

        if (alreadyLiked) {
            // 🔴 UNLIKE
            commentLikeRepo.deleteByComment_IdAndUser_Id(
                    commentId,
                    user.getId()
            );
        } else {
            // ❤️ LIKE
            CommentLike like = new CommentLike();
            like.setComment(comment);
            like.setUser(user);
            commentLikeRepo.save(like);
        }
    }




    /* ================= REPLY TO COMMENT ================= */

    @Override
    public void replyToComment(Integer commentId, String content, String loggedInEmail) {

        Comment parent = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userRepo.findByEmail(loggedInEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment reply = new Comment();
        reply.setContent(content);
        reply.setParent(parent);
        reply.setPost(parent.getPost());
        reply.setUser(user);

        commentRepo.save(reply);
    }

    /* ================= MAPPER ================= */

    private CommentDto mapToDto(Comment comment, User currentUser) {

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        dto.setOwner(
            comment.getUser() != null &&
            comment.getUser().getId().equals(currentUser.getId())
        );

        dto.setUserName(comment.getUser().getName());

        // ❤️ likes count
        dto.setLikes(
            (int) commentLikeRepo.countByComment_Id(comment.getId())
        );

        // 👍 liked by current user or not
        dto.setLiked(
            commentLikeRepo.existsByComment_IdAndUser_Id(
                comment.getId(),
                currentUser.getId()
            )
        );

        // replies
        dto.setReplies(
            comment.getReplies()
                   .stream()
                   .map(reply -> mapToDto(reply, currentUser))
                   .toList()
        );

        return dto;
    }



    @Override
    public List<CommentDto> getCommentsByPost(int postId, String email) {

        User currentUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return commentRepo.findByPostIdAndParentIsNull(postId)
                .stream()
                .map(comment -> mapToDto(comment, currentUser))
                .toList();
    }



    @Override
    public List<CommentDto> getCommentsByPost(int postId) {

        String loggedInEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return getCommentsByPost(postId, loggedInEmail);
    }

    @Override
    public List<CommentDto> getCommentsByPost(Integer postId) {
        return getCommentsByPost(postId.intValue());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, int postId, String email) {
        return addComment(postId, commentDto.getContent(), email);
    }


    @Override
    public List<CommentDto> getCommentsByUser(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return commentRepo.findByUser(user)
                .stream()
                .map(comment -> mapToDto(comment, user))
                .toList();
    }


    @Override
    public void addComment(Integer postId, String content) {

        String loggedInEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        addComment(postId, content, loggedInEmail);
    }

}
