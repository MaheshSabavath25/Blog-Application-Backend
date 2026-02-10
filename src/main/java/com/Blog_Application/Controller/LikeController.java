package com.Blog_Application.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Blog_Application.BlogServices.LikeService;
import com.Blog_Application.BlogServices.PostService;
import com.Blog_Application.Payload.PostDto;
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    // ✅ LIKE / UNLIKE POST (RETURN UPDATED POST)
    @PostMapping("/like/{postId}")
    public ResponseEntity<Boolean> toggleLike(
            @PathVariable int postId,
            Principal principal
    ) {
        boolean liked = likeService.toggleLike(postId, principal.getName());
        return ResponseEntity.ok(liked);
    }


}
