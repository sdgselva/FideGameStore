package com.fidegamestore.repository;

import com.fidegamestore.domain.Constante;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConstanteRepository extends JpaRepository<Constante,Integer> {
    @Query(nativeQuery = true,
        value = "SELECT * FROM constante c WHERE c.atributo = :constante ;")
     public Optional<Constante> findByAtributo(@Param("constante") String constante);
}
