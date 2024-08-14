package com.example.we_save.domain.post.repository;

import com.example.we_save.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 국내 게시글 최신순 조회 (상황 종료 제외)
    @Query("SELECT p FROM Post p WHERE p.hearts >= 10 AND p.status != 'COMPLETED' ORDER BY p.createAt DESC")
    List<Post> findRecentDomesticPostsExcludingCompleted(Pageable pageable);

    // 국내 게시글 최신순 조회 (상황 종료 포함)
    @Query("SELECT p FROM Post p WHERE p.hearts >= 10 ORDER BY p.createAt DESC")
    List<Post> findRecentDomesticPosts(Pageable pageable);

    // 국내 게시글 확인순 조회 (상황 종료 제외)
    @Query("SELECT p FROM Post p WHERE p.hearts >= 10 AND p.status != 'COMPLETED' ORDER BY p.hearts DESC")
    List<Post> findTopDomesticPostsExcludingCompleted(Pageable pageable);

    // 국내 게시글 확인순 조회 (상황 종료 포함)
    @Query("SELECT p FROM Post p WHERE p.hearts >= 10 ORDER BY p.hearts DESC")
    List<Post> findTopDomesticPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate AND p.region.id = :regionId AND p.status != 'COMPLETED' ORDER BY p.createAt DESC")
    List<Post> findRecentPostsExcludingCompleted(@Param("startDate") LocalDateTime startDate,
                                                 @Param("regionId") Long regionId,
                                                 Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate AND p.region.id = :regionId AND p.status != 'COMPLETED' ORDER BY p.hearts DESC")
    List<Post> findTopNearPostsExcludingCompleted(@Param("startDate") LocalDateTime startDate,
                                                  @Param("regionId") Long regionId,
                                                  Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.region.id = :regionId AND p.status != 'COMPLETED' ORDER BY ST_Distance_Sphere(POINT(p.longitude, p.latitude), POINT(:longitude, :latitude))")
    List<Post> findDistanceNearPostsExcludingCompleted(@Param("regionId") Long regionId,
                                                       @Param("longitude") double longitude,
                                                       @Param("latitude") double latitude,
                                                       Pageable pageable);

    // 생성 시간 기준으로 최근 N개의 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate AND p.region.id = :regionId ORDER BY p.createAt DESC")
    List<Post> findRecentPosts(@Param("startDate") LocalDateTime startDate,
                               @Param("regionId") Long regionId,
                               Pageable pageable);

    // 인기순으로 N개의 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate ORDER BY p.hearts DESC")
    List<Post> findTopPosts(@Param("startDate") LocalDateTime startDate,
                            Pageable pageable);

    // 인기순으로 N개의 사건 사고 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.createAt >= :startDate AND p.region.id = :regionId ORDER BY p.hearts DESC")
    List<Post> findTopNearPosts(@Param("startDate") LocalDateTime startDate,
                                @Param("regionId") Long regionId,
                                Pageable pageable);

    // 거리순으로 N개의 사건 사고 게시물 조회
    @Query("SELECT p FROM Post p WHERE p.region.id = :regionId ORDER BY ST_Distance_Sphere(POINT(p.longitude, p.latitude), POINT(:longitude, :latitude))")
    List<Post> findDistanceNearPosts(@Param("regionId") Long regionId,
                                     @Param("longitude") double longitude, @Param("latitude") double latitude, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId ORDER BY p.createAt DESC")
    List<Post> findAllByUser(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:query% OR p.content LIKE %:query%) " +
            "AND p.region.id = :regionId " +
            "AND (:excludeCompleted = false OR p.status != 'COMPLETED') " +
            "ORDER BY p.createAt DESC")
    List<Post> searchPostsByKeywordRecentNearby(@Param("query") String query,
                                                @Param("regionId") Long regionId,
                                                @Param("excludeCompleted") boolean excludeCompleted,
                                                Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:query% OR p.content LIKE %:query%) " +
            "AND p.region.id = :regionId " +
            "AND (:excludeCompleted = false OR p.status != 'COMPLETED') " +
            "ORDER BY p.hearts DESC")
    List<Post> searchPostsByKeywordTopNearby(@Param("query") String query,
                                             @Param("regionId") Long regionId,
                                             @Param("excludeCompleted") boolean excludeCompleted,
                                             Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:query% OR p.content LIKE %:query%) " +
            "AND p.region.id = :regionId " +
            "AND (:excludeCompleted = false OR p.status != 'COMPLETED') " +
            "ORDER BY ST_Distance_Sphere(POINT(p.longitude, p.latitude), POINT(:longitude, :latitude))")
    List<Post> searchPostsByKeywordDistance(@Param("query") String query,
                                            @Param("regionId") Long regionId,
                                            @Param("excludeCompleted") boolean excludeCompleted,
                                            @Param("longitude") double longitude,
                                            @Param("latitude") double latitude,
                                            Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:query% OR p.content LIKE %:query%) " +
            "AND (:excludeCompleted = false OR p.status != 'COMPLETED') " +
            "ORDER BY p.createAt DESC")
    List<Post> searchPostsByKeywordRecentDomestic(@Param("query") String query,
                                                  @Param("excludeCompleted") boolean excludeCompleted,
                                                  Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:query% OR p.content LIKE %:query%) " +
            "AND (:excludeCompleted = false OR p.status != 'COMPLETED') " +
            "ORDER BY p.hearts DESC")
    List<Post> searchPostsByKeywordTopDomestic(@Param("query") String query,
                                               @Param("excludeCompleted") boolean excludeCompleted,
                                               Pageable pageable);
}
