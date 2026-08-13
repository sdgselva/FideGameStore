package com.fidegamestore.service;

import com.fidegamestore.domain.Region;
import com.fidegamestore.repository.RegionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
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
    
        
    @Transactional(readOnly = true)
    public Optional<Region> getRegion(Integer idRegion) {
        return regionRepository.findById(idRegion);
    }
    
    @Transactional
    public void save(Region region) {
        region = regionRepository.save(region);
    }

    @Transactional
    public void delete(Integer idRegion) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!regionRepository.existsById(idRegion)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La categoría con ID " + idRegion + " no existe.");
        }
        try {
            regionRepository.deleteById(idRegion);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la region. Tiene datos asociados.", e);
        }
    }
}