package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.domain.post.entity.Post;
import com.example.we_save.domain.post.entity.PostImage;
import com.example.we_save.domain.post.repository.PostImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostImageServiceImpl implements PostImageService {

    private final PostImageRepository postImageRepository;

    public PostImageServiceImpl(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Override
    public void savePostImages(List<MultipartFile> files, Post post) throws IOException {
        //실제 파일 서버 경로
        String projectPath = "/home/upload/post/"+post.getId();

        //로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/"+post.getId();
        Path directoryPath = Paths.get(projectPath);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Save each file
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            PostImage postImage = new PostImage();
            postImage.setPost(post);
            postImage.setName(fileName);
            postImage.setFilePath("/files/post/" + post.getId() + fileName);
            postImageRepository.save(postImage);
        }
    }

    @Override
    public void deletePostImage(long imageId, Post post) throws IOException {

    }

    @Override
    public void deletePostAllImage(Post post) throws IOException {

    }
}
