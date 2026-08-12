package com.fidegamestore.service;

import com.fidegamestore.domain.Region;
import com.fidegamestore.repository.RegionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegionService {
    private final RegionRepository regionRepository;

    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }
    
    @Transactional(readOnly=true)
    public List<Region> getRegions() {
        return regionRepository.selectAllRegions();
    }
}