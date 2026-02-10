package com.Blog_Application.Services.Imple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Blog_Application.BlogServices.LikeService;
import com.Blog_Application.Entities.Post;
import com.Blog_Application.Entities.PostLike;
import com.Blog_Application.Entities.User;
import com.Blog_Application.Exception.ResourceNotFoundException;
import com.Blog_Application.Repository.PostLikeRepo;
import com.Blog_Application.Repository.PostRepo;
import com.Blog_Application.Repositorys.UserRepository;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private PostLikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public boolean toggleLike(int postId, String email) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return likeRepo.findByPostAndUser(post, user)
                .map(like -> {
                    likeRepo.delete(like); // UNLIKE
                    return false;
                })
                .orElseGet(() -> {
                    PostLike like = new PostLike();
                    like.setPost(post);
                    like.setUser(user);
                    likeRepo.save(like); // LIKE
                    return true;
                });
    }

    @Override
    public long getLikeCount(int postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return likeRepo.countByPost(post);
    }

    @Override
    public boolean isPostLikedByUser(int postId, String email) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return likeRepo.findByPostAndUser(post, user).isPresent();
    }
}
