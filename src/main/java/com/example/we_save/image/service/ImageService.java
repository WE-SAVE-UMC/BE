package com.example.we_save.image.service;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    public Image saveDefaultProfileImage();
    public Image saveProfileImage(MultipartFile file, User user) throws IOException;
    public void deleteProfileImage(long imageId,User user) throws IOException ;
}
