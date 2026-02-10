package com.Blog_Application.BlogServices;

public interface LikeService {

    boolean toggleLike(int postId, String email);

    long getLikeCount(int postId);

    boolean isPostLikedByUser(int postId, String email);
}
