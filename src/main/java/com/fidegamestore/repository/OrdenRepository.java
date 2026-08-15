package com.fidegamestore.repository;

import com.fidegamestore.domain.Orden;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdenRepository extends JpaRepository<Orden, Integer>{
    @Query("SELECT o FROM Orden o " +
            "LEFT JOIN FETCH o.usuario " +       // Carga usuario
            "LEFT JOIN FETCH o.estadoOrden eo " +       // Carga las ordenesLlave que tengan el mismo ID
            "LEFT JOIN FETCH o.ordenLlaves ol " +       // Carga las ordenesLlave que tengan el mismo ID
            "LEFT JOIN FETCH ol.llave l " +        // Carga la llave de cada OrdenLlave
            "LEFT JOIN FETCH l.varianteProducto vp " +      // Carga el varianteProducto de cada llave
            "LEFT JOIN FETCH vp.producto " +      // Carga el producto de varianteProducto
            "LEFT JOIN FETCH vp.region " +      //Carga la region de varianteProducto
            "LEFT JOIN FETCH vp.plataforma " +      // Carga la plataforma de varianteProducto
            "WHERE o.idOrden = :idOrden")
    Optional<Orden> findByIdOrdenConDetalle(@Param("idOrden") Integer idOrden);
    
    @Query("SELECT o FROM Orden o " +
            "LEFT JOIN FETCH o.usuario " +       // Carga usuario
            "LEFT JOIN FETCH o.estadoOrden eo " +       // Carga las ordenesLlave que tengan el mismo ID
            "LEFT JOIN FETCH o.ordenLlaves ol " +       // Carga las ordenesLlave que tengan el mismo ID
            "LEFT JOIN FETCH ol.llave l " +        // Carga la llave de cada OrdenLlave
            "LEFT JOIN FETCH l.varianteProducto vp " +      // Carga el varianteProducto de cada llave
            "LEFT JOIN FETCH vp.producto " +      // Carga el producto de varianteProducto
            "LEFT JOIN FETCH vp.region " +      //Carga la region de varianteProducto
            "LEFT JOIN FETCH vp.plataforma ")      // Carga la plataforma de varianteProducto
    List<Orden> findAllOrdenes();  
}
