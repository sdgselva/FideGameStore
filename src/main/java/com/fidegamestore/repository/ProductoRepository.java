package com.fidegamestore.repository;

import com.fidegamestore.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByActivoTrue();
    
    @Query("SELECT p FROM Producto p " +
        "LEFT JOIN FETCH p.categoria c " +       // Carga las ordenesLlave que tengan el mismo ID
        "WHERE c.idCategoria = :idCategoria ")
    List<Producto> findProductoByIdCategoria(@Param("idCategoria") Integer idCategoria);
}

