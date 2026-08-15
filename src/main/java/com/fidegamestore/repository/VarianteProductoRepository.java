package com.fidegamestore.repository;

import com.fidegamestore.domain.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Integer> {

    public List<VarianteProducto> findAll();

    Optional<VarianteProducto> findByProducto_IdProductoAndRegion_IdRegionAndPlataforma_IdPlataforma(
            Integer idProducto,
            Integer idRegion,
            Integer idPlataforma
    );
}
