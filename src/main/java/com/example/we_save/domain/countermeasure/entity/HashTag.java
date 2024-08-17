package com.example.we_save.domain.countermeasure.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class HashTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id = 0L;

    @Column(nullable = false, length = 10)
    private String tagName;
}
