package com.fidegamestore.repository;

import com.fidegamestore.domain.Anuncio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {
    @Query(nativeQuery = true,
                value = "SELECT * FROM anuncio a WHERE a.activo=true;")
    public List<Anuncio> findByActivoTrue();
}
