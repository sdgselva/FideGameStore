package com.fidegamestore.service;

import com.fidegamestore.domain.Plataforma;
import com.fidegamestore.repository.PlataformaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlataformaService {
    private final PlataformaRepository plataformaRepository;

    public PlataformaService(PlataformaRepository plataformaRepository) {
        this.plataformaRepository = plataformaRepository;
    }
    
    @Transactional(readOnly=true)
    public List<Plataforma> getPlataformas() {
        return plataformaRepository.findAllPlataformas();
    }
}
