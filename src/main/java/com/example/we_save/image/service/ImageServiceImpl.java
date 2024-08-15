package com.example.we_save.image.service;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import com.example.we_save.image.entity.Image;
import com.example.we_save.image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;


    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Image saveProfileImage(MultipartFile file,User user) throws IOException {

        String projectPath = "/home/upload/user/"+user.getId();

        UUID uuid = UUID.randomUUID();

        String fileName = uuid+"_"+file.getOriginalFilename();

        // 저장 경로가 없다면 생성
        Path directoryPath = Paths.get(projectPath);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath); // 경로가 없다면 생성
        }

        File saveFile = new File(projectPath,fileName);
        file.transferTo(saveFile);

        Image image = new Image();
        image.setName(fileName);
        image.setFilePath("/files/user/"+user.getId()+ "/" + fileName);

        return imageRepository.save(image);
    }
    @Override
    public void deleteProfileImage(long imageId, User user) throws IOException {
        user.setProfileImage(null);
        userRepository.save(user); // Save the updated user

        String projectPath = "/home/upload/user/" + user.getId();
        Path directoryPath = Paths.get(projectPath);

        if (Files.exists(directoryPath)) {
            Files.walk(directoryPath)
                    .map(Path::toFile)
                    .forEach(File::delete);

            Files.deleteIfExists(directoryPath);
        }

        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            imageRepository.delete(image);
        }
    }

    @Override
    public List<Image> savePostImages(List<MultipartFile> files, Post post) throws IOException {
        String projectPath = "/home/upload/post/"+post.getId();
        Path directoryPath = Paths.get(projectPath);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
        List<Image> savedImages = new ArrayList<>();

        // Save each file
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            Image image = new Image();
            image.setName(fileName);
            image.setFilePath("/files/post/" + post.getId() + fileName);
            image = imageRepository.save(image);

            savedImages.add(image);
        }

        return savedImages;
    }

    @Override
    public void deletePostImage(long imageId, Post post) throws IOException {

    }

    @Override
    public void deletePostAllImage(Post post) throws IOException {

    }
}
