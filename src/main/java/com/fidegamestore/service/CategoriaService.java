package com.fidegamestore.service;

import com.fidegamestore.domain.Categoria;
import com.fidegamestore.repository.CategoriaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
    
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { //Sólo activos...
            return categoriaRepository.findByActivoTrue();
        }

        return categoriaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }
    
    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Integer idCategoria) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoria. Tiene datos asociados.", e);
        }
    }
}
