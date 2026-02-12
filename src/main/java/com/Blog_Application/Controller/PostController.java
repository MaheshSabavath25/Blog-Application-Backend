package com.Blog_Application.Controller;


import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Blog_Application.BlogServices.PostService;
import com.Blog_Application.Payload.PostDto;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

   

    // ✅ CREATE POST
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(
            @PathVariable Integer categoryId) {

        return ResponseEntity.ok(
                postService.getPostsByCategory(categoryId)
        );
    }
    
    
    @PostMapping("/category/{categoryId}")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @PathVariable int categoryId,
            Principal principal) {

        String email = principal.getName();

        PostDto createdPost =
                postService.createPost(postDto, email, categoryId);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }



    // ✅ GET ALL POSTS
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(Principal principal) {

        String email = null;
        if (principal != null) {
            email = principal.getName();
        }

        return ResponseEntity.ok(
                email != null
                    ? postService.getAllPosts(email)
                    : postService.getAllPosts()
        );
    }

    
 // ✅ UPDATE POST
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(
            @RequestBody PostDto postDto,
            @PathVariable Integer postId
    ) {
        PostDto updatedPost = postService.updatePost(postDto, postId);
        return ResponseEntity.ok(updatedPost);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Integer postId,
            Principal principal) {

        String email = null;
        if (principal != null) {
            email = principal.getName();
        }

        return ResponseEntity.ok(
                postService.getPostById(postId, email)
        );
    }



    // ✅ DELETE POST
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @PathVariable Integer postId) {

        postService.deletePost(postId);
        return ResponseEntity.ok("Post deleted successfully");
    }

    // ✅ GET POSTS OF LOGGED-IN USER
    @GetMapping("/user/me")
    public ResponseEntity<List<PostDto>> getMyPosts(
            Principal principal) {

        String email = principal.getName();
        return ResponseEntity.ok(
                postService.getPostsByEmail(email));
    }

    // ✅ UPLOAD IMAGE
    @PostMapping("/{postId}/image")
    public ResponseEntity<PostDto> uploadImage(
            @PathVariable Integer postId,
            @RequestParam("image") MultipartFile image)
            throws IOException {

        PostDto post =
                postService.uploadPostImage(postId, image);

        return ResponseEntity.ok(post);
    }

    // ✅ DOWNLOAD IMAGE
    

    
    @GetMapping("/paged")
    public ResponseEntity<Page<PostDto>> getPostsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                postService.getAllPostsPaged(page, size)
        );
    }
    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPosts(
            @PathVariable String keyword) {

        return ResponseEntity.ok(
                postService.searchPosts(keyword)
        );
    }
    
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<PostDto>> getPostsByHashtag(
            @PathVariable String tag
    ) {
        return ResponseEntity.ok(postService.getPostsByHashtag(tag));
    }





}
