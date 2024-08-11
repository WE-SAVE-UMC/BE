package com.example.we_save.domain.countermeasure.repository;

import com.example.we_save.domain.countermeasure.entity.Countermeasure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountermeasureRepository extends JpaRepository<Countermeasure, Long> {
}
