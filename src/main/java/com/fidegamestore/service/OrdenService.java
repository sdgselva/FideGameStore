package com.fidegamestore.service;

import com.fidegamestore.domain.*;
import com.fidegamestore.repository.OrdenRepository;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Transactional(readOnly = true) // Solo lectura, mejor rendimiento
    public Orden getOrdenConDetalle(Integer idOrden) {

        // 1. Llamar al método del repositorio que usa FETCH JOIN
        // El .orElseThrow lanza una excepción si el Optional está vacío
        return ordenRepository.findByIdOrdenConDetalle(idOrden)
                .orElseThrow(() -> new NoSuchElementException("Orden con ID " + idOrden + " no encontrada."));

        // El resultado es una Orden con todas las relaciones (usuario, ventas, producto) 
        // ya inicializadas, lista para ser usada en el Controller y la vista Thymeleaf.
    }

    @Transactional(readOnly = true) // Solo lectura, mejor rendimiento
    public List<Orden> getOrdenesPorUsername(String username) {
        return ordenRepository.findByIdOrdenPorUsername(username);
    }

    @Transactional(readOnly = true) // Solo lectura, mejor rendimiento
    public List<Orden> getAllOrdenes() {
        return ordenRepository.findAllOrdenes();
    }
}
