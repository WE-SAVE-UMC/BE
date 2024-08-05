package com.example.we_save.domain.post.controller.request;

import java.util.List;

public class PostRequestDto {
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private String status;
    private double x;
    private double y;
    private String postRegionName;
    private List<String> images;
    private boolean report119;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPostRegionName() {
        return postRegionName;
    }

    public void setPostRegionName(String postRegionName) {
        this.postRegionName = postRegionName;
    }

    public boolean isReport119() {
        return report119;
    }

    public void setReport119(boolean report119) {
        this.report119 = report119;
    }
}
