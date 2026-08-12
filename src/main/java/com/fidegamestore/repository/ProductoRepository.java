package com.fidegamestore.repository;

import com.fidegamestore.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query(nativeQuery = true,
                value = "SELECT * from producto p where p.activo = true;")
    public List<Producto> findByActivoTrue();
}
