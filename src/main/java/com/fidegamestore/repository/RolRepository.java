package com.fidegamestore.repository;

import com.fidegamestore.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RolRepository extends JpaRepository<Rol, Integer>  {
    @Query(nativeQuery = true,
        value = "SELECT * from rol r WHERE r.nombre_rol = :nombreRol ;")
     public Optional<Rol> findByNombreRol(@Param("nombreRol") String nombreRol);
}
