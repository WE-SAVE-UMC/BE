package com.example.we_save.image.service;

import com.example.we_save.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    public Image saveProfileImage(MultipartFile file,long userId) throws IOException;
    public void deleteProfileImage(long imageId,long userId) throws IOException ;
}
