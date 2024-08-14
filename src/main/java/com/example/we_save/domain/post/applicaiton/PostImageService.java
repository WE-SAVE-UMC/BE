package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostImage;
import com.example.we_save.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostImageService {
    public void savePostImages(List<MultipartFile> files, Post post) throws IOException;
    public void deletePostImage(long imageId,Post post) throws IOException;
    public void deletePostAllImage(long postId) throws IOException;
}
