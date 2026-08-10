package com.fidegamestore.repository;

import com.fidegamestore.domain.Ruta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RutaRepository extends JpaRepository<Ruta, Integer>{
    @Query(nativeQuery = true,
                value = "SELECT * from ruta r order by r.requiere_rol ASC;")
        public List<Ruta> selectAllRutasOrderByRequiredRoleAsc();
}
