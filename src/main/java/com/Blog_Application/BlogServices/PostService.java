package com.Blog_Application.BlogServices;



import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.Blog_Application.Entities.Category;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;
import com.Blog_Application.Payload.PostDto;


public interface PostService {

    // create
    PostDto createPost(PostDto postDto, String email, int categoryId);

    // update
    PostDto updatePost(PostDto postDto, int postId);

    // delete
    void deletePost(int postId);

    // get
    
    List<PostDto> getAllPosts();

    // filter
    List<PostDto> getPostsByCategory(int categoryId);

    List<PostDto> getPostsByUser(int userId);
    
    List<PostDto> getPostsByEmail(String email);

	PostDto uploadImage(Integer postId, MultipartFile image, String imagePath) throws IOException;

	PostDto uploadPostImage(Integer postId, MultipartFile image) throws IOException;

	Page<PostDto> getAllPostsPaged(int page, int size);

	List<PostDto> searchPosts(String keyword);

	List<PostDto> getPostsByHashtag(String tag);

	PostDto getPostById(int postId, String email);

	List<PostDto> getAllPosts(String email);

	

}
