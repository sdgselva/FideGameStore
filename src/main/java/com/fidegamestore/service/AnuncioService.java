package com.fidegamestore.service;

import com.fidegamestore.domain.Anuncio;
import com.fidegamestore.repository.AnuncioRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnuncioService {
    private final AnuncioRepository anuncioRepository;
    
    public AnuncioService(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Anuncio> getAnuncios(boolean activo) {
        if (activo) { //Sólo activos...            
            return anuncioRepository.findByActivoTrue();
        }
        return anuncioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Anuncio> getAnuncio(Integer idAnuncio) {
        return anuncioRepository.findById(idAnuncio);
    }
    
    @Transactional(readOnly = true)
    public List<Anuncio> findAnuncioByIdCategoria(Integer idCategoria) {
        return anuncioRepository.findAnuncioByIdCategoria(idCategoria);
    }
    
    @Transactional
    public void save(Anuncio anuncio) {
        anuncio = anuncioRepository.save(anuncio);
    }
    
    @Transactional
    public void delete(Integer idAnuncio) {
        // Verifica si el anuncio existe antes de intentar eliminarlo
        if (!anuncioRepository.existsById(idAnuncio)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("El anuncio con ID " + idAnuncio + " no existe.");
        }
        try {
            anuncioRepository.deleteById(idAnuncio);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el anuncio. Tiene datos asociados.", e);
        }
    }
}
