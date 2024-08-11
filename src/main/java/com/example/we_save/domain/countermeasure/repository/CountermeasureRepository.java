package com.example.we_save.domain.countermeasure.repository;

import com.example.we_save.domain.countermeasure.entity.Countermeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountermeasureRepository extends JpaRepository<Countermeasure, Long> {

    List<Countermeasure> findAllByTitleContainingOrMainContentContaining(String titleKeyword, String mainContentKeyword);
}
