package com.example.we_save.apiPayload.util;

import com.example.we_save.domain.region.entity.EupmyeondongRegion;
import com.example.we_save.domain.region.entity.SidoRegion;
import com.example.we_save.domain.region.entity.SigunguRegion;
import com.example.we_save.domain.region.repository.EupmyeondongRepository;
import com.example.we_save.domain.region.repository.SidoRepository;
import com.example.we_save.domain.region.repository.SigunguRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RegionUtil {

    private final EupmyeondongRepository eupmyeondongRepository;
    private final SigunguRepository sigunguRepository;
    private final SidoRepository sidoRepository;

    private static final double EARTH_RADIUS_KM = 6371.0;

    public String getFullRegionName(long eupmyeondongId) {

        EupmyeondongRegion eupmyeondongRegion = eupmyeondongRepository.findById(eupmyeondongId)
                .orElseThrow(() -> new EntityNotFoundException("EupmyeondongRegion not found"));

        if (eupmyeondongRegion.getSigungu() == null || eupmyeondongRegion.getSido() == null) {
            throw new EntityNotFoundException("Region not found");
        }

        SigunguRegion sigunguRegion = sigunguRepository.findById(eupmyeondongRegion.getSigungu().getId())
                .orElseThrow(() -> new EntityNotFoundException("SigunguRegion not found"));
        SidoRegion sidoRegion = sidoRepository.findById(eupmyeondongRegion.getSido().getId())
                .orElseThrow(() -> new EntityNotFoundException("SidoRegion not found"));

        return String.format("%s %s %s",
                sidoRegion.getRegionName(),
                sigunguRegion.getRegionName(),
                eupmyeondongRegion.getRegionName()
        ).trim();
    }

    public long convertRegionNameToRegionId(String regionName) {

        // regionName의 세부 주소까지 입력으로 들어올 경우 예외처리
        String regionParts[] = regionName.split("\\s+");
        if (regionParts.length > 3) {
            regionName = regionParts[0] + " " + regionParts[1] + " " + regionParts[2];
        }

        EupmyeondongRegion eupmyeondongRegion = eupmyeondongRepository.findByRegionFullName(regionName)
                .orElseThrow(() -> new EntityNotFoundException("Region not found"));

        return eupmyeondongRegion.getId();
    }

    public static double calculateDistanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public static String extractRegionBeforeSecondSpace(String fullRegionName) {

        String[] parts = fullRegionName.split("\\s+"); // 최대 3개의 부분으로 분리
        if (parts.length < 2) {
            throw new IllegalArgumentException();
        }

        return parts[0] + " " + parts[1];
    }

    public static String extractRegionAfterSecondSpace(String fullRegionName) {

        String[] parts = fullRegionName.split("\\s+"); // 최대 3개의 부분으로 분리
        if (parts.length < 2) {
            throw new IllegalArgumentException();
        }

        StringBuilder regionName = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (i > 2) {
                regionName.append(" "); // 각 부분 사이에 공백 추가
            }
            regionName.append(parts[i]);
        }

        return regionName.toString();
    }

    public static String extractEupMyeonDong(String fullRegionName) {
        String[] parts = fullRegionName.split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException();
        }
        // parts[2]가 읍면동
        return parts[2];
    }
}
