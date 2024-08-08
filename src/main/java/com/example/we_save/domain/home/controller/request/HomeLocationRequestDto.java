package com.example.we_save.domain.home.controller.request;

import lombok.Data;

@Data
public class HomeLocationRequestDto {
    double latitude;
    double longitude;
    String regionName;
}
