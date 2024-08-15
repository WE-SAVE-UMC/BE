package com.example.we_save.domain.comment.application;

import com.example.we_save.domain.comment.entity.Comment;
import com.example.we_save.domain.comment.entity.CommentImage;
import com.example.we_save.domain.comment.repository.CommentImageRepository;
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
public class CommentImageServiceImpl implements CommentImageService {

    private final CommentImageRepository commentImageRepository;

    private static final int MAX_IMAGE_COUNT = 10;

    public CommentImageServiceImpl(CommentImageRepository commentImageRepository) {
        this.commentImageRepository = commentImageRepository;
    }

    @Override
    public void saveCommentImage(List<MultipartFile> files, Comment comment) throws IOException {
        if (files.size() > MAX_IMAGE_COUNT) {
            throw new IllegalArgumentException("최대 10개의 이미지만 첨부할 수 있습니다.");
        }

        // 실제 파일 서버 경로
        String projectPath = "/home/upload/comment/" + comment.getId();

        // 로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/comments/" + comment.getId();
        Path directoryPath = Paths.get(projectPath);
        if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            CommentImage commentImage = new CommentImage();
            commentImage.setComment(comment);
            commentImage.setName(fileName);
            commentImage.setFilePath("/files/comments/" + comment.getId() + fileName);
            commentImageRepository.save(commentImage);
        }
    }

    @Override
    public void deleteCommentAllImage(long commentId) throws IOException {
        // 실제 파일 서버 경로
        String projectPath = "/home/upload/comment/" + commentId;

        // 로컬 서버 경로
        //String projectPath = System.getProperty("user.dir") + "/media/comments/" + commentId;
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