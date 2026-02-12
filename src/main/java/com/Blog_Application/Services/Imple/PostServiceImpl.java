package com.Blog_Application.Services.Imple;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Blog_Application.BlogServices.PostService;
import com.Blog_Application.Entities.Category;
import com.Blog_Application.Entities.Hashtag;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.User;
import com.Blog_Application.Exception.ResourceNotFoundException;
import com.Blog_Application.Payload.PostDto;
import com.Blog_Application.Repository.CategoryRepo;
import com.Blog_Application.Repository.HashtagRepo;
import com.Blog_Application.Repository.LikeRepository;
import com.Blog_Application.Repository.PostRepo;
import com.Blog_Application.Repositorys.UserRepository;
import com.cloudinary.Cloudinary;

import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;
    
   

    
    @Autowired
    private Cloudinary cloudinary;


    @Autowired
    private LikeRepository likeRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashtagRepo hashtagRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String imagePath;

    /* ================= HASHTAG ================= */

    private Set<Hashtag> processHashtags(String text) {
        Set<Hashtag> tags = new HashSet<>();
        if (text == null) return tags;

        for (String word : text.split("\\s+")) {
            if (word.startsWith("#")) {
                String name = word.substring(1).toLowerCase();

                Hashtag tag = hashtagRepo.findByName(name)
                        .orElseGet(() -> {
                            Hashtag h = new Hashtag();
                            h.setName(name);
                            return hashtagRepo.save(h);
                        });

                tags.add(tag);
            }
        }
        return tags;
    }

    private Set<String> mapHashtagsToDto(Post post) {
        if (post.getHashtags() == null) return Set.of();

        return post.getHashtags()
                .stream()
                .map(Hashtag::getName)
                .collect(Collectors.toSet());
    }

    /* ================= DTO MAPPERS ================= */

    // BASE mapper (NO user context)
    private PostDto mapToDto(Post post) {
        PostDto dto = modelMapper.map(post, PostDto.class);
        dto.setHashtags(mapHashtagsToDto(post));
        dto.setLikeCount(likeRepo.countByPost(post));
        return dto;
    }

    // USER-AWARE mapper (LIKE / UNLIKE)
    private PostDto mapToDto(Post post, String email) {

        PostDto dto = mapToDto(post);

        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                dto.setLiked(likeRepo.existsByPostAndUser(post, user));
            }
        }

        return dto;
    }

    /* ================= CREATE ================= */

    @Override
    public PostDto createPost(PostDto postDto, String email, int categoryId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Category category;

        // ✅ if categoryId is provided → use existing
        if (categoryId > 0) {
            category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        }
        // ✅ if categoryId is 0 → auto-create from title
        else {
            category = getOrCreateCategory(
                    postDto.getCategory().getCategoryTitle()
            );
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(user);
        post.setCategory(category);
        post.setHashtags(processHashtags(postDto.getContent()));

        return mapToDto(postRepo.save(post), email);
    }


    /* ================= UPDATE ================= */

    @Override
    public PostDto updatePost(PostDto postDto, int postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setHashtags(processHashtags(postDto.getContent()));

        return mapToDto(postRepo.save(post));
    }

    /* ================= DELETE ================= */

    @Override
    public void deletePost(int postId) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        postRepo.delete(post);
    }

    /* ================= GET ================= */

    @Override
    public PostDto getPostById(int postId, String email) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        return mapToDto(post, email);
    }

    @Override
    public List<PostDto> getAllPosts(String email) {
        return postRepo.findAll()
                .stream()
                .map(post -> mapToDto(post, email))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByCategory(int categoryId) {

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        return postRepo.findByCategory(category)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUser(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return postRepo.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return postRepo.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /* ================= PAGINATION ================= */

    @Override
    public Page<PostDto> getAllPostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String email = getLoggedInEmail();

        return postRepo.findAll(pageable)
                .map(post -> mapToDto(post, email));
    }


    /* ================= SEARCH ================= */

    @Override
    public List<PostDto> searchPosts(String keyword) {
        String email = getLoggedInEmail();

        return postRepo
            .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword)
            .stream()
            .map(post -> mapToDto(post, email))
            .collect(Collectors.toList());
    }


    /* ================= HASHTAG FILTER ================= */

    @Override
    public List<PostDto> getPostsByHashtag(String tag) {
        String email = getLoggedInEmail();

        return postRepo.findByHashtags_Name(tag.toLowerCase())
                .stream()
                .map(post -> mapToDto(post, email))
                .collect(Collectors.toList());
    }


    /* ================= IMAGE UPLOAD ================= */

    @Override
    public PostDto uploadPostImage(Integer postId, MultipartFile image) throws IOException {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        File folder = new File(imagePath);
        if (!folder.exists()) folder.mkdirs();

        String fileName = image.getOriginalFilename();
        String fullPath = imagePath + File.separator + fileName;

        Files.copy(image.getInputStream(), Paths.get(fullPath),
                StandardCopyOption.REPLACE_EXISTING);

        post.setImageName(fileName);
        return mapToDto(postRepo.save(post));
    }

    @Override
    public PostDto uploadImage(Integer postId, MultipartFile image, String imagePath) throws IOException {
        return uploadPostImage(postId, image);
    }

    

    @Override
    public List<PostDto> getAllPosts() {
        String email = getLoggedInEmail();

        return postRepo.findAll()
                .stream()
                .map(post -> mapToDto(post, email))
                .collect(Collectors.toList());
    }

    
    private String getLoggedInEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return null;
    }

    /* ================= CATEGORY ================= */

    private Category getOrCreateCategory(String categoryTitle) {

        return categoryRepo
                .findByCategoryTitleIgnoreCase(categoryTitle.trim())
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setCategoryTitle(categoryTitle.trim());
                    category.setCategoryDescription("Auto-created category");
                    return categoryRepo.save(category);
                });
    }

    


	

}
