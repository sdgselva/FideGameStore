package com.fidegamestore.repository;

import com.fidegamestore.domain.Llave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface LlaveRepository extends JpaRepository<Llave, Integer>{
    @Query(nativeQuery = true,
        value = "SELECT * from llave l WHERE l.id_variante_producto = :idVarianteProducto;")
    public Llave findByIdVarianteProducto (@Param("idVarianteProducto") int idVarianteProducto);
}
