package com.example.we_save.domain.post.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(nullable = false, length = 10)
    private String name;
}
