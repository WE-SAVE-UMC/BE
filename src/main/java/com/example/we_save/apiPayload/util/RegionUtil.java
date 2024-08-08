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
}
