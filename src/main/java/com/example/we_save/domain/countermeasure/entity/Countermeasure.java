package com.example.we_save.domain.countermeasure.entity;

import com.example.we_save.apiPayload.code.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Countermeasure extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private String mainContent;
    private String detailContent;
}
