package com.fidegamestore.service;

import com.fidegamestore.domain.Plataforma;
import com.fidegamestore.repository.PlataformaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
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
    
    @Transactional(readOnly=true)
    public List<Plataforma> getPlataformasActivas() {
        return plataformaRepository.findByActivoTrue();
    }
    
     @Transactional(readOnly = true)
    public Optional<Plataforma> getPlataforma(Integer idPlataforma) {
        return plataformaRepository.findById(idPlataforma);
    }
    
    @Transactional
    public void save(Plataforma plataforma) {
        plataforma = plataformaRepository.save(plataforma);
    }

    @Transactional
    public void delete(Integer idPlataforma) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!plataformaRepository.existsById(idPlataforma)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La categoría con ID " + idPlataforma + " no existe.");
        }
        try {
            plataformaRepository.deleteById(idPlataforma);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la plataforma. Tiene datos asociados.", e);
        }
    }
}
