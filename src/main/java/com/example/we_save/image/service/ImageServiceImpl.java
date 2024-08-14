package com.example.we_save.image.service;

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
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;


    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    @Override
    public Image saveProfileImage(MultipartFile file,long userId) throws IOException {

        String projectPath = "/home/upload/user/"+userId;

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
        image.setFilePath("/files/user/"+userId+ "/" + fileName);

        return imageRepository.save(image);
    }
    @Override
    public void deleteProfileImage(long userId) throws IOException {
        String projectPath = "/home/upload/user/" + userId;
        Path directoryPath = Paths.get(projectPath);

        if (Files.exists(directoryPath)) {
            Files.walk(directoryPath)
                    .map(Path::toFile)
                    .forEach(File::delete);

            Files.deleteIfExists(directoryPath);
        }
    }
}
