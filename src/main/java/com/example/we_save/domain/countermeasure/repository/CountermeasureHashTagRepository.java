package com.example.we_save.domain.countermeasure.repository;

import com.example.we_save.domain.countermeasure.entity.CountermeasureHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountermeasureHashTagRepository extends JpaRepository<CountermeasureHashTag, Long> {

    List<CountermeasureHashTag> findAllByHashTag_TagName(String hashTag);
}
