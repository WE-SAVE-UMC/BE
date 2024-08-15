package com.example.we_save.domain.comment.application;

import com.example.we_save.domain.comment.entity.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CommentImageService {
    public void saveCommentImage(List<MultipartFile> files, Comment comment) throws IOException;
    public void deleteCommentAllImage(long commentId) throws IOException;
}
