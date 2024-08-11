package com.example.we_save.domain.countermeasure.repository;

import com.example.we_save.domain.countermeasure.entity.Countermeasure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountermeasureRepository extends JpaRepository<Countermeasure, Long> {

    @Query(value = "SELECT *, MATCH(title, main_content) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE) AS score "
            + "FROM countermeasure "
            + "WHERE MATCH(title, main_content) AGAINST(:searchTerm IN NATURAL LANGUAGE MODE) "
            + "ORDER BY score DESC",
            nativeQuery = true)
   List<Countermeasure> findAllByTitleContainingOrMainContentContaining(String searchTerm);
}
