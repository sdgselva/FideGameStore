package com.fidegamestore.service;

import com.fidegamestore.domain.Ruta;
import com.fidegamestore.repository.RutaRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaService {
    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }
    
    @Transactional(readOnly=true)
    public List<Ruta> getRutas() {
        return rutaRepository.selectAllRutasOrderByRequiredRoleAsc();
    }
    
    @Transactional(readOnly = true)
    public Ruta getRuta(Integer idRuta) {
        return rutaRepository.findById(idRuta).orElseThrow(
            () -> new NoSuchElementException("Ruta con ID " + idRuta + " no encontrada."));
    }

    @Transactional
    public void save(Ruta ruta) {
        rutaRepository.save(ruta);
    }

    @Transactional
    public void delete(Integer idRuta) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!rutaRepository.existsById(idRuta)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La Ruta con ID " + idRuta + " no existe.");
        }
        try {
            rutaRepository.deleteById(idRuta);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la ruta. Tiene datos asociados.", e);
        }
    }
}
