package com.example.we_save.domain.post.controller.response;

import com.example.we_save.domain.comment.controller.response.CommentDto;
import com.example.we_save.domain.post.entity.PostStatus;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDtoWithComments {
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private PostStatus status;
    private double x;
    private double y;
    private String postRegionName;
    private int hearts;
    private int dislikes;
    private int comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
    private List<CommentDto> commentsList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getHearts() {
        return hearts;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public String getPostRegionName() {
        return postRegionName;
    }

    public void setPostRegionName(String postRegionName) {
        this.postRegionName = postRegionName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<CommentDto> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<CommentDto> commentsList) {
        this.commentsList = commentsList;
    }
}
