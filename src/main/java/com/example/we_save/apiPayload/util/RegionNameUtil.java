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
public class RegionNameUtil {

    private final EupmyeondongRepository eupmyeondongRepository;
    private final SigunguRepository sigunguRepository;
    private final SidoRepository sidoRepository;

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
}
