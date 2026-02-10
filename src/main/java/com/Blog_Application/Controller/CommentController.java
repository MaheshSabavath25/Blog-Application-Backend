package com.Blog_Application.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Blog_Application.BlogServices.CommentService;
import com.Blog_Application.Payload.CommentDto;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 🔹 GET COMMENTS (OWNER FLAG DEPENDS ON LOGGED-IN USER)
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(
            @PathVariable int postId,
            Principal principal
    ) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                commentService.getCommentsByPost(postId, principal.getName())
        );
    }

    // 🔹 ADD COMMENT
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable int postId,
            @RequestBody CommentDto dto,
            Principal principal
    ) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                commentService.addComment(postId, dto.getContent(), principal.getName())
        );
    }

    // 🔥 DELETE COMMENT (OWNER ONLY)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable int commentId,
            Principal principal
    ) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        commentService.deleteComment(commentId, principal.getName());
        return ResponseEntity.ok("Deleted");
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable int commentId,
            @RequestBody CommentDto dto,
            Principal principal
    ) {
        CommentDto updated =
                commentService.updateComment(commentId, dto.getContent(), principal.getName());
        return ResponseEntity.ok(updated);
    }
    
 // ✅ GET MY COMMENTS (LOGGED-IN USER)
    @GetMapping("/me")
    public ResponseEntity<List<CommentDto>> getMyComments(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                commentService.getCommentsByUser(principal.getName())
        );
    }
    
    @PostMapping("/like/{commentId}")
    public void likeComment(
            @PathVariable Integer commentId,
            Principal principal
    ) {
        commentService.toggleCommentLike(commentId, principal.getName());
    }

    @PostMapping("/reply/{commentId}")
    public void replyToComment(
            @PathVariable Integer commentId,
            @RequestBody CommentDto dto,
            Principal principal
    ) {
        commentService.replyToComment(
                commentId,
                dto.getContent(),
                principal.getName() // email from JWT
        );
    }





}
