package com.example.we_save.domain.countermeasure.entity;

import jakarta.persistence.*;

@Entity
public class CountermeasureHashTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id = 0L;

    @Column(nullable = false, length = 10)
    private String tagName;
}
