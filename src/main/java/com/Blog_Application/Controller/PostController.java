package com.Blog_Application.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Blog_Application.BlogServices.FileService;
import com.Blog_Application.BlogServices.PostService;
import com.Blog_Application.Payload.PostDto;


import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Value("${project.image}")
    private String imagePath;

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
    @GetMapping("/image/{imageName}")
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response) throws IOException {

        String fullPath = imagePath + File.separator + imageName;
        File file = new File(fullPath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 🔥 Detect correct content type automatically
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);

        InputStream is = new FileInputStream(file);
        StreamUtils.copy(is, response.getOutputStream());
    }

    
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
