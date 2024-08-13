package com.example.we_save.domain.post.controller.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyPostRequestDto {
    private double latitude;
    private double longitude;
    private String regionName;
}
