package com.example.we_save.domain.post.applicaiton;

import com.example.we_save.domain.post.controller.response.NearbyPostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostScheduler {

    @Autowired
    private PostService postService;

    private List<NearbyPostResponseDto> cachedTopPosts;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void updateTopPostsCache() {
        cachedTopPosts = postService.getTop5RecentPostsWithin24Hours();
        System.out.println("Top 5 recent posts have been updated: " + cachedTopPosts);
    }

    public List<NearbyPostResponseDto> getCachedTopPosts() {
        return cachedTopPosts;
    }
}
