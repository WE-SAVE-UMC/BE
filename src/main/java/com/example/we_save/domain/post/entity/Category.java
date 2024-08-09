package com.example.we_save.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {
    FIRE("화재"),
    EARTHQUAKE("지진"),
    HEAVY_RAIN("폭우"),
    HEAVY_SNOW("폭설"),
    TRAFFIC_ACCIDENT("교통사고"),
    OTHER("기타");

    private final String value;

    public static Category of(String value) {
        for (Category category : Category.values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException();
    }
}