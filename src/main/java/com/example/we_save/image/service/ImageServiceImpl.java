package com.example.we_save.image.service;

import com.example.we_save.domain.user.entity.User;
import com.example.we_save.domain.user.repository.UserRepository;
import com.example.we_save.image.entity.Image;
import com.example.we_save.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public Image saveDefaultProfileImage() {
        Image image = new Image();
        image.setName("default_profile.jpg");
        image.setFilePath("/files/user/default_profile.jpg");

        return imageRepository.save(image);
    }

    @Override
    public Image saveProfileImage(MultipartFile file,User user) throws IOException {

        String projectPath = "/home/upload/user/"+user.getId();

        //로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/"+user.getId();

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

        //로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/"+user.getId();

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

}
