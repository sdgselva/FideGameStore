package com.fidegamestore.repository;

import com.fidegamestore.domain.Plataforma;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlataformaRepository extends JpaRepository<Plataforma, Integer>{
    @Query(nativeQuery = true,
                value = "SELECT * from plataforma p;")
    public List<Plataforma> findAllPlataformas();
    
    @Query(nativeQuery = true,
                value = "SELECT * FROM categoria c WHERE c.activo = true;")
    public List<Plataforma> findByActivoTrue();  
}
