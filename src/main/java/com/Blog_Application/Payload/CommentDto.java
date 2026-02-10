package com.Blog_Application.Payload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Integer id;

    private String content;

    // 👤 Commenter name
    private String userName;

    // ⏰ Created time (USE Date, NOT LocalDateTime)
    private Date createdAt;

    // 🔐 Owner flag (for edit/delete menu)
    private boolean owner;
    
   
    private boolean liked;
    
    private long likes;   // 🔥 CHANGE HERE

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }


	

	

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}
	
	private List<CommentDto> replies = new ArrayList<>();

	public List<CommentDto> getReplies() {
	    return replies;
	}

	public void setReplies(List<CommentDto> replies) {
	    this.replies = replies;
	}

    
    
}
