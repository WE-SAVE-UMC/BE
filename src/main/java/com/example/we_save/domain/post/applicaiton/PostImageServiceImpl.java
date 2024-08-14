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

    private static final int MAX_IMAGE_COUNT = 10;

    public PostImageServiceImpl(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Override
    public void savePostImages(List<MultipartFile> files, Post post) throws IOException {

        if (files.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        //실제 파일 서버 경로
        String projectPath = "/home/upload/post/"+post.getId();

        //로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/"+post.getId();
        Path directoryPath = Paths.get(projectPath);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

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


    // 게시글 삭제 시 해당 게시글의 모든 이미지를 저장한 폴더를 삭제
    @Override
    public void deletePostAllImage(long postId) throws IOException {
        //실제 파일 서버 경로
        String projectPath = "/home/upload/post/" + postId;
        //로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/"+postId;
        Path directoryPath = Paths.get(projectPath);

        // 디렉토리가 존재하면 폴더 및 하위 파일들 삭제
        if (Files.exists(directoryPath)) {
            // 폴더 자체를 삭제 (폴더 안에 파일만 있는 경우)
            Files.walk(directoryPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        Files.deleteIfExists(directoryPath);
    }
}
