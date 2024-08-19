package com.example.we_save.domain.countermeasure.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CountermeasureHashTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id = 0L;

    @ManyToOne
    @JoinColumn(name = "countermeasure_id", nullable = false)
    private Countermeasure countermeasure;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private HashTag hashTag;
}
